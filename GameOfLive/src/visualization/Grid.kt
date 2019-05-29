package visualization


import java.util.Arrays
import java.util.Random

class Grid(n: Int, m: Int) {

    private var cells: Array<Array<Cell>>? = null
    var numberOfRows: Int = 0
        private set
    var numberOfColumns: Int = 0
        private set

    init {
        resize(n, m)
    }

    internal fun initializeCells() {
        cells = Array(numberOfRows) { Array(numberOfColumns) {Cell()} }

    }

    fun updateCell(i: Int, j: Int) {
        cells!![i][j].negateAlive()
    }

    fun nextGeneration() {
        goToNextState(calculateNextState())
    }

    private fun goToNextState(nextState: Array<BooleanArray>) {
        for (rowIndex in 0 until numberOfRows) {
            for (columnIndex in 0 until numberOfColumns) {
                getCell(rowIndex, columnIndex).isAlive = nextState[rowIndex][columnIndex]
            }
        }
    }

    private fun calculateNextState(): Array<BooleanArray> {
        val nextState = Array(numberOfRows) { BooleanArray(numberOfColumns) }

        for (i in 0 until numberOfRows) {
            for (j in 0 until numberOfColumns) {
                val cell = getCell(i, j)
                val numberOfAliveNeighbours = countAliveNeighbours(i, j)
                val isAliveInNextState = cell.isAlive && numberOfAliveNeighbours == 2 || numberOfAliveNeighbours == 3
                nextState[i][j] = isAliveInNextState
            }
        }

        return nextState
    }

    private fun countAliveNeighbours(rowIndex: Int, columnIndex: Int): Int {
        return getNeighbours(rowIndex, columnIndex)
                .stream()
                .filter(Cell::isAlive)
                .count().toInt()
    }

    private fun getNeighbours(rowIndex: Int, columnIndex: Int): List<Cell> {
        val north = rowIndex - 1
        val east = columnIndex + 1
        val south = rowIndex + 1
        val west = columnIndex - 1

        return Arrays.asList(
                getCell(north, west),
                getCell(north, columnIndex),
                getCell(north, east),
                getCell(rowIndex, east),
                getCell(south, east),
                getCell(south, columnIndex),
                getCell(south, west),
                getCell(rowIndex, west)
        )
    }

    fun getCell(rowIndex: Int, columnIndex: Int): Cell {
        return cells!![getPeriodicRow(rowIndex)][getPeriodicColumn(columnIndex)]
    }

    private fun getPeriodicRow(rowIndex: Int): Int {
        return (rowIndex + numberOfRows) % numberOfRows
    }

    private fun getPeriodicColumn(columnIndex: Int): Int {
        return (columnIndex + numberOfColumns) % numberOfColumns
    }

    fun makeEveryCellDead() {
        for (i in 0 until numberOfRows) {
            for (j in 0 until numberOfColumns) {
                getCell(i, j).isAlive = false
            }
        }
    }

    fun makeGlider() {
        val iCenter = numberOfRows / 2
        val jCenter = numberOfColumns / 2
        getCell(iCenter, jCenter).isAlive = true
        getCell(iCenter, jCenter - 1).isAlive = true
        getCell(iCenter - 1, jCenter).isAlive = true
        getCell(iCenter - 1, jCenter + 1).isAlive = true
        getCell(iCenter + 1, jCenter + 1).isAlive = true
    }

    fun makeOscillator() {
        val iCenter = numberOfRows / 2
        val jCenter = numberOfColumns / 2
        getCell(iCenter, jCenter).isAlive = true
        getCell(iCenter + 1, jCenter).isAlive = true
        getCell(iCenter - 1, jCenter).isAlive = true

    }

    fun makeBeehive() {
        val iCenter = numberOfRows / 2
        val jCenter = numberOfColumns / 2
        getCell(iCenter, jCenter - 1).isAlive = true
        getCell(iCenter - 1, jCenter).isAlive = true
        getCell(iCenter + 1, jCenter).isAlive = true
        getCell(iCenter + 1, jCenter + 1).isAlive = true
        getCell(iCenter, jCenter + 2).isAlive = true
        getCell(iCenter - 1, jCenter + 1).isAlive = true

    }

    fun makeRandom() {
        val random = Random()
        for (i in 0 until numberOfRows)
            for (j in 0 until numberOfColumns)
                getCell(i, j).isAlive = random.nextBoolean()
    }

    fun resize(n: Int, m: Int) {
        numberOfRows = n
        numberOfColumns = m
        initializeCells()
    }
}

