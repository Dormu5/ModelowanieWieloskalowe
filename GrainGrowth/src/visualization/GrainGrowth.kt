package visualization

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.util.Duration

class GrainGrowth(n: Int, m: Int) {

    val grid: Grid
    private var timeline: Timeline? = null
    private var nucleationType: NucleationType? = null
    private var numberOfGrains: Int = 0
    private var homogeneousRows: Int = 0
    private var homogeneousColumns: Int = 0
    private var playable: Boolean = false
    private val radius: Int = 0

    init {
        grid = Grid(n, m)
        buildGrid()
        setTimeline()
    }

    private fun setTimeline() {
        val eventHandler:EventHandler<ActionEvent> = EventHandler{ next() }
        val keyFrame = KeyFrame(Duration(1000.0), eventHandler)
        timeline = Timeline(keyFrame)
        timeline!!.cycleCount = Animation.INDEFINITE
    }

    fun resizeGrid(n: Int, m: Int) {
        grid.resize(n, m)
    }

    private operator fun next() {
        grid.nextGeneration()
    }

    private fun buildGrid() {
        grid.initializeCells()
    }

    fun playGame() {
        if (playable) timeline!!.play()
    }

    fun stopGame() {
        if (playable) timeline!!.stop()
    }

    fun setInstance(numberOfGrains: Int, boundaryCondition: BoundaryCondition,
                    nucleationType: NucleationType, growthType: GrowthType,
                    homogeneousRows: Int, homogeneousColumns: Int, radius: Int) {
        stopGame()
        this.numberOfGrains = numberOfGrains
        this.nucleationType = nucleationType
        this.homogeneousRows = homogeneousRows
        this.homogeneousColumns = homogeneousColumns
        grid.setRadius(radius)
        grid.setBoundaryCondition(boundaryCondition)
        grid.setGrowthType(growthType)
    }

    fun doNucleationType() {
        when (nucleationType) {
            NucleationType.Custom -> grid.setGrainsCustom(numberOfGrains)
            NucleationType.Homogeneous -> grid.setGrainsHomogeneous(homogeneousRows, homogeneousColumns)
            NucleationType.Random -> grid.setGrainsRandom(numberOfGrains)
            NucleationType.WithRadius -> grid.setGrainsWithRadius(numberOfGrains)
        }
    }

    fun setPlayable() {
        this.playable = true
    }
}
