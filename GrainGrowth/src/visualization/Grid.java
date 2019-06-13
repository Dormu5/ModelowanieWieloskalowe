package visualization;


import javafx.scene.text.Text;

import java.io.FileWriter;
import java.io.IOException;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class Grid {

    private Cell[][] cells;
    private double[] dislocation;
    private int numberOfRows;
    private int numberOfColumns;
    private BoundaryCondition boundaryCondition;
    private NeighborhoodType neighborhoodType;


    private NucleationType nucleationType;
    int counterGrain = 1;
    int numberOfGrains = 0;
    private boolean[][] oldState;
    private Random generator;
    private double radiusNucleation;
    private double radiusNeighborhood;
    private boolean noChanges = true;
    private int drxIteration = 1;
    private List<Cell> borderCells;
    public Grid(int n, int m) {
        generator = new Random();
        resize(n, m);
        borderCells = new LinkedList<>();
    }

    void initializeCells() {
        cells = new Cell[numberOfRows][numberOfColumns];
        oldState = new boolean[numberOfRows][numberOfColumns];
        int id = 1;
        for (int i = 0; i < numberOfRows; i++)
            for (int j = 0; j < numberOfColumns; j++) {
                cells[i][j] = new Cell(generator.nextDouble() + i, generator.nextDouble() + j);
                cells[i][j].setId(id++);
            }
    }

    public void updateCell(int i, int j) {
        cells[i][j].negateAlive();
    }

    public boolean nextGeneration() {
        noChanges = true;
        goToNextState(calculateNextState());
        return noChanges;
    }

    private void goToNextState(boolean[][] nextState) {
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j < getNumberOfColumns(); j++) {
                if (oldState[i][j] != nextState[i][j] && nextState[i][j] == true) {
                    noChanges = false;
                    getCell(i, j).setAlive(true);
                }
            }
        }
        oldState = nextState;
    }

    private boolean[][] calculateNextState() {
        boolean[][] nextState = new boolean[getNumberOfRows()][getNumberOfColumns()];

        IntStream.range(0, getNumberOfRows()).parallel().forEach(i -> {
            IntStream.range(0, getNumberOfColumns()).parallel().forEach(j -> {
                Cell cell = getCell(i, j);
                if (!cell.isAlive()) {
                    int nextGrainType = checkNextGrainType(i, j);
                    if (nextGrainType != -1) {
                        cell.setGrainNumber(nextGrainType);
                        nextState[i][j] = true;
                    } else
                        nextState[i][j] = false;
                } else
                    nextState[i][j] = true;
            });
        });
        return nextState;
    }

    private int checkNextGrainType(int rowIndex, int columnIndex) {
        List<Cell> collect = getNeighbours(rowIndex, columnIndex).stream()
                .filter(Cell::isAlive).collect(Collectors.toList());
        if (collect.size() > 0) {
            Map<Integer, Integer> map = new HashMap<>();
            for (Cell cell : collect) {
                int grainNumber = cell.getGrainNumber();
                if (map.containsKey(grainNumber)) {
                    map.put(grainNumber, map.get(grainNumber) + 1);
                } else {
                    map.put(cell.getGrainNumber(), 1);
                }
            }
            OptionalInt max = map.values().stream().mapToInt(v -> v).max();
            Optional<Integer> integerStream = map.entrySet().stream().filter(entry -> max.getAsInt() == entry.getValue()).map(Map.Entry::getKey).findFirst();
            return integerStream.get();
        } else return -1;

    }

    private List<Cell> getNeighbours(int rowIndex, int columnIndex) {
        int north = rowIndex - 1;
        int east = columnIndex + 1;
        int south = rowIndex + 1;
        int west = columnIndex - 1;
        switch (neighborhoodType) {
            case VonNeumann: {
                return Arrays.asList(
                        getCell(north, columnIndex),
                        getCell(south, columnIndex),
                        getCell(rowIndex, east),
                        getCell(rowIndex, west)
                );
            }
            case Moore: {
                return Arrays.asList(
                        getCell(north, west),
                        getCell(north, columnIndex),
                        getCell(north, east),
                        getCell(rowIndex, east),
                        getCell(south, east),
                        getCell(south, columnIndex),
                        getCell(south, west),
                        getCell(rowIndex, west)
                );
            }
            case Pentagonal_Random: {
                int pentagonalRandom = generator.nextInt(4);
                switch (pentagonalRandom) {
                    case 0:
                        return pentagonalLeft(north, east, south, west, columnIndex, rowIndex);
                    case 1:
                        return pentagonalRight(north, east, south, west, columnIndex, rowIndex);
                    case 2:
                        return pentagonalUp(north, east, south, west, columnIndex, rowIndex);
                    case 3:
                        return pentagonalDown(north, east, south, west, columnIndex, rowIndex);
                }
            }
            case Hexagonal_Left: {
                return hexagonalLeft(north, east, south, west, columnIndex, rowIndex);
            }
            case Hexagonal_Right: {
                return hexagonalRight(north, east, south, west, columnIndex, rowIndex);
            }
            case Hexagonal_Random: {
                int hexagonalRandom = generator.nextInt(2);

                switch (hexagonalRandom) {
                    case 0:
                        return hexagonalLeft(north, east, south, west, columnIndex, rowIndex);
                    case 1:
                        return hexagonalRight(north, east, south, west, columnIndex, rowIndex);
                }
            }

            default:
                return Arrays.asList();
        }
    }

    public void nextMonteCarlo(double kt) {
        for (int i = 0; i < getNumberOfRows(); i++) {
            for (int j = 0; j < getNumberOfColumns(); j++) {
                int ri=generator.nextInt(getNumberOfRows());
                int rj=generator.nextInt(getNumberOfColumns());
                Cell cell = getCell(ri, rj);
                List<Cell> neighbours = getNeighbours(ri, rj);
                List<Cell> neighboursFiltered = neighbours.stream().filter(c -> c.getGrainNumber() != cell.getGrainNumber()).collect(Collectors.toList());
                List<Integer> neighboursNumbers = neighboursFiltered.stream().filter(c -> c.getGrainNumber() != 0).map(Cell::getGrainNumber).collect(Collectors.toList());
                if (neighboursNumbers.size() > 1) {
                    long energyBefore = neighboursFiltered.size();
                    int newNumber = neighboursNumbers.get(generator.nextInt(neighboursNumbers.size()));
                    long energyAfter = neighbours.stream().filter(c -> newNumber != c.getGrainNumber()).count();
                    cell.setEnergy(energyAfter - energyBefore);
                    if (cell.getEnergy() <= 0) {
                        cell.setChanged(true);
                        borderCells.add(cell);
                        cell.setGrainNumber(newNumber);
                        cell.negateAlive();
                    } else {
                        double p = Math.exp(-cell.getEnergy() / kt);
                        double percent = Math.random();
                        if (percent < p) {
                            cell.setChanged(true);
                            borderCells.add(cell);
                            cell.setGrainNumber(newNumber);
                            cell.negateAlive();
                        }
                    }
                } else {
                    cell.setChanged(false);
                    borderCells.remove(cell);
                    cell.negateAlive();
                }
            }
        }
    }
    public void nextDRX() {
        double deltaRo = dislocation[drxIteration] - dislocation[(drxIteration++) - 1];
        double averageRo = Math.abs(deltaRo / (numberOfColumns * numberOfRows));
        double percentOfRo = DRXConsts.DISTRIBUTION_PERCENT.getValue$growth() * averageRo;
        double criticalRo = DRXConsts.CRITICAL_RO.getValue$growth() /(numberOfColumns * numberOfRows);
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                getCell(i, j).addDensity(percentOfRo);
                deltaRo -= percentOfRo;
            }
        }
        percentOfRo = deltaRo/100;
        while (deltaRo >= 0) {
            Cell cell;
            double percent = Math.random();
            if(percent < 0.8){
                cell = borderCells.get(generator.nextInt(borderCells.size()));
            }else {
                while(true) {
                    int n = generator.nextInt(numberOfRows);
                    int m = generator.nextInt(numberOfColumns);
                    cell = getCell(n, m);
                    if (borderCells.contains(cell)) {
                        continue;
                    }
                    break;
                }
            }
            deltaRo -= percentOfRo;
            cell.addDensity(percentOfRo);
        }
        for (int i = 0; i < numberOfRows; i++) {
            for (int j = 0; j < numberOfColumns; j++) {
                Cell cell = getCell(i, j);
                if (cell.getDensity() > criticalRo && cell.isChanged()) {
                    cell.setCrystalized(true);
                    cell.setDensity(0);
                    cell.negateAlive();
                }
            }
        }


        if(drxIteration >= dislocation.length){
            try(FileWriter csvWriter = new FileWriter("new.csv")){
                saveDislocationToFile(csvWriter);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }


    }

    private void saveDislocationToFile(FileWriter csvWriter) throws IOException {
        csvWriter.append("time");
        csvWriter.append(";");
        csvWriter.append("density");
        csvWriter.append("\n");
        int time = 0;
        for(int i = 0; i < dislocation.length; i++){
            csvWriter.append(time++ + ";" + dislocation[i]);
            csvWriter.append("\n");
        }
        csvWriter.flush();
    }




    public void calculateDislocation(int iterations) {
        final double A = DRXConsts.A.getValue$growth();
        final double B = DRXConsts.B.getValue$growth();
        dislocation = new double[iterations+1];
        dislocation[0] = 1;
        int i = 1;
        for (double t = 0.001; t < (iterations+1) / 1000.0; t += 0.001) {
            dislocation[i++] = A/B+((1.0 - (A / B))*Math.exp(-B*t));
        }
    }




    private List<Cell> hexagonalRight(int north, int east, int south, int west, int columnIndex, int rowIndex) {
        return Arrays.asList(
                getCell(north, columnIndex),
                getCell(north, east),
                getCell(rowIndex, east),
                getCell(south, columnIndex),
                getCell(south, west),
                getCell(rowIndex, west)
        );
    }

    private List<Cell> hexagonalLeft(int north, int east, int south, int west, int columnIndex, int rowIndex) {
        return Arrays.asList(
                getCell(north, west),
                getCell(north, columnIndex),
                getCell(rowIndex, east),
                getCell(south, east),
                getCell(south, columnIndex),
                getCell(rowIndex, west)
        );


    }

    private List<Cell> pentagonalDown(int north, int east, int south, int west, int columnIndex, int rowIndex) {
        return Arrays.asList(
                getCell(rowIndex, east),
                getCell(south, east),
                getCell(south, columnIndex),
                getCell(south, west),
                getCell(rowIndex, west)
        );
    }

    private List<Cell> pentagonalUp(int north, int east, int south, int west, int columnIndex, int rowIndex) {
        return Arrays.asList(
                getCell(north, west),
                getCell(north, columnIndex),
                getCell(north, east),
                getCell(rowIndex, east),
                getCell(rowIndex, west)
        );
    }

    private List<Cell> pentagonalRight(int north, int east, int south, int west, int columnIndex, int rowIndex) {
        return Arrays.asList(
                getCell(north, west),
                getCell(north, columnIndex),
                getCell(south, columnIndex),
                getCell(south, west),
                getCell(rowIndex, west)
        );
    }

    private List<Cell> pentagonalLeft(int north, int east, int south, int west, int columnIndex, int rowIndex) {
        return Arrays.asList(
                getCell(north, columnIndex),
                getCell(north, east),
                getCell(rowIndex, east),
                getCell(south, east),
                getCell(south, columnIndex)
        );
    }

    public void resize(int n, int m) {
        numberOfRows = n;
        numberOfColumns = m;
        initializeCells();
    }

    public void setGrainsRandom(int numberOfGrains) {
        for (int i = 1; i <= numberOfGrains; i++) {
            int row = generator.nextInt(numberOfRows);
            int column = generator.nextInt(numberOfColumns);
            if (!getCell(row, column).isAlive()) {
                cells[row][column].setGrainNumber(i);
                cells[row][column].negateAlive();
            } else i--;
        }
    }

    public void setGrainsCustom(int numberOfGrains) {
        this.numberOfGrains = numberOfGrains;
        counterGrain = 1;
    }

    public void setGrainsHomogeneous(int homogeneousRows, int homogeneousColumns) {
        if (numberOfRows >= homogeneousRows && numberOfColumns >= homogeneousColumns
                && numberOfColumns * numberOfRows >= homogeneousColumns * homogeneousRows) {
            int rowStep = (int) (numberOfRows / (double) homogeneousRows);
            int columnStep = (int) (numberOfColumns / (double) homogeneousColumns);
            int counter = 1;
            for (int i = 0; i < homogeneousRows; i++) {
                for (int j = 0; j < homogeneousColumns; j++) {
                    cells[rowStep * i + rowStep / 2][columnStep * j + columnStep / 2].setGrainNumber(counter++);
                    cells[rowStep * i + rowStep / 2][columnStep * j + columnStep / 2].negateAlive();
                }
            }

        }

    }



    public boolean setColorIdOnClick(int ii, int jj) {
        if (nucleationType.equals(NucleationType.Custom)) {
            if (cells[ii][jj].getGrainNumber() != 0) {
                cells[ii][jj].setGrainNumber(0);
                counterGrain--;
                return true;
            }
            if (counterGrain <= numberOfGrains) {
                cells[ii][jj].setGrainNumber(counterGrain++);
                return true;
            }
        }
        return false;
    }

    public void setBoundaryCondition(BoundaryCondition boundaryCondition) {
        this.boundaryCondition = boundaryCondition;
    }

    public void setNeighborhoodType(NeighborhoodType neighborhoodType) {
        this.neighborhoodType = neighborhoodType;
    }

    public Cell getCell(int rowIndex, int columnIndex) {
        switch (boundaryCondition) {
            case Absorbing:
                if (rowIndex < 0 || rowIndex >= numberOfRows || columnIndex < 0 || columnIndex >= numberOfColumns)
                    return new Cell(0, 0);
                else
                    return cells[rowIndex][columnIndex];
            case Periodic:
                return cells[getPeriodicRow(rowIndex)][getPeriodicColumn(columnIndex)];
            default:
                return null;

        }

    }

    private int getPeriodicRow(int rowIndex) {
        return (rowIndex + getNumberOfRows()) % getNumberOfRows();
    }

    private int getPeriodicColumn(int columnIndex) {
        return (columnIndex + getNumberOfColumns()) % getNumberOfColumns();
    }

    public int getNumberOfRows() {
        return numberOfRows;
    }

    public int getNumberOfColumns() {
        return numberOfColumns;
    }

    public void setRadiusNucleation(double radiusNucleation) {
        this.radiusNucleation = radiusNucleation;
    }

    public void setRadiusNeighborhood(double radiusNeighborhood) {
        this.radiusNeighborhood = radiusNeighborhood;
    }

    public void setNumberOfGrains(int numberOfGrains) {
        this.numberOfGrains = numberOfGrains;
    }

    public void setNucleationType(NucleationType nucleationType) {
        this.nucleationType = nucleationType;
    }

    public Cell[][] getCells() {
        return cells;
    }

    public void clearDislocation() {
        for(int i = 0 ; i < numberOfRows; i++){
            for(int j = 0; j < numberOfColumns; j++){
                getCell(i,j).setDensity(0);
                getCell(i,j).setCrystalized(false);
                getCell(i,j).negateAlive();
                drxIteration = 1;
            }
        }
    }
}

