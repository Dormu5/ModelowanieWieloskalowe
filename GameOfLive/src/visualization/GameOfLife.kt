package visualization

import javafx.animation.Animation
import javafx.animation.KeyFrame
import javafx.animation.Timeline
import javafx.beans.property.ReadOnlyLongWrapper
import javafx.event.ActionEvent
import javafx.event.EventHandler
import javafx.util.Duration

class GameOfLife(n: Int, m: Int) {

    val grid: Grid = Grid(n, m)
    private var timeline: Timeline? = null
    private var state: States? = null

    init {
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

    fun updateState(newState: States) {
        this.state = newState
        changeBoard()
    }

    private fun changeBoard() {
        cleanBoard()
        when (state) {
            States.Glider -> makeGliderOnGrid()
            States.Random -> makeRandomOnGrid()
            States.Beehive -> makeBeehiveOnGrid()
            States.Oscillator -> makeOscillatorOnGrid()

        }
    }

    private fun makeGliderOnGrid() {
        grid.makeGlider()
    }

    private fun makeRandomOnGrid() {
        grid.makeRandom()
    }

    private fun makeBeehiveOnGrid() {
        grid.makeBeehive()
    }

    private fun makeOscillatorOnGrid() {
        grid.makeOscillator()
    }

    private fun cleanBoard() {
        stopGame()
        grid.makeEveryCellDead()
    }

    private fun buildGrid() {
        grid.initializeCells()
    }

    fun updateCell(i: Int, j: Int) {
        grid.updateCell(i, j)
    }

    fun playGame() {
        timeline!!.play()
    }

    fun stopGame() {
        timeline!!.stop()
    }
}
