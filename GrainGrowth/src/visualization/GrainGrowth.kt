package visualization

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.scene.text.Text
import javafx.util.Duration

class GrainGrowth(n: Int, m: Int, private val errorText: Text) {

    val grid: Grid
    private var timeline: Timeline? = null
    private var nucleationType: NucleationType? = null
    private var numberOfGrains: Int = 0
    private var homogeneousRows: Int = 0
    private var homogeneousColumns: Int = 0
    var isPlayable: Boolean = false
        private set
    private val radius: Int = 0
    private var finished = false
    val cells: Array<Array<Cell>>
        get() = grid.cells

    init {
        grid = Grid(n, m)
        buildGrid()
    }

    private fun setTimeline() {
        val eventHandler:EventHandler<ActionEvent> = EventHandler{ next() }
        val keyFrame = KeyFrame(Duration(1000.0), eventHandler)
        timeline = Timeline(keyFrame)
        timeline!!.cycleCount = Animation.INDEFINITE
    }

    private fun setTimelineMonteCarlo(kt: Int, iterations: Int) {
        val eventHandler:EventHandler<ActionEvent> = EventHandler{ nextMonteCarlo(kt) }
        val keyFrame = KeyFrame(Duration(1000.0), eventHandler)
        timeline = Timeline(keyFrame)
        timeline!!.cycleCount = iterations
        timeline!!.play()
    }

    private fun nextMonteCarlo(kt: Int) {
        grid.nextMonteCarlo()
    }

    fun resizeGrid(n: Int, m: Int) {
        grid.resize(n, m)
    }

    private operator fun next() {
        if (!finished)
            finished = grid.nextGeneration()
        else {
            timeline!!.stop()
            println("end")
        }

    }

    private fun buildGrid() {
        grid.initializeCells()
    }

    fun playGame() {
        if (isPlayable) timeline!!.play()
    }

    fun stopGame() {
        if (isPlayable) timeline!!.stop()
    }

    fun setInstance(numberOfGrains: Int, boundaryCondition: BoundaryCondition,
                    nucleationType: NucleationType, neighborhoodType: NeighborhoodType,
                    homogeneousRows: Int, homogeneousColumns: Int, radiusNucleation: Double, radiusNeighborhood: Double) {
        stopGame()
        setTimeline()
        finished = false
        this.numberOfGrains = numberOfGrains
        this.nucleationType = nucleationType
        this.homogeneousRows = homogeneousRows
        this.homogeneousColumns = homogeneousColumns
        grid.setNumberOfGrains(numberOfGrains)
        grid.setRadiusNucleation(radiusNucleation)
        grid.setRadiusNeighborhood(radiusNeighborhood)
        grid.setBoundaryCondition(boundaryCondition)
        grid.setNeighborhoodType(neighborhoodType)
        grid.setNucleationType(nucleationType)
    }

    fun doNucleationType() {
        when (nucleationType) {
            NucleationType.Custom -> grid.setGrainsCustom(numberOfGrains)
            NucleationType.Homogeneous -> grid.setGrainsHomogeneous(homogeneousRows, homogeneousColumns)
            NucleationType.Random -> grid.setGrainsRandom(numberOfGrains)

        }
    }

    fun setPlayable() {
        this.isPlayable = true
    }

    fun monteCarlo(neighborhoodType: NeighborhoodType, kt: Int, iterations: Int) {
        if (finished) {
            println("started MonteCarlo")
            grid.setNeighborhoodType(neighborhoodType)
            setTimelineMonteCarlo(kt, iterations)
        }
    }
}
