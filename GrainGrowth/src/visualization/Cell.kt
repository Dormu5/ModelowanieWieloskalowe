package visualization

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

class Cell {

    private val alive = SimpleBooleanProperty(false)
    var grainNumber = -1
    private var centerOfGravityX: Double = 0.toDouble()
    private var centerOfGravityY: Double = 0.toDouble()

    var isAlive: Boolean
        get() = alive.get()
        set(alive) = this.alive.set(alive)

    fun aliveProperty(): BooleanProperty {
        return alive
    }

    fun negateAlive() {
        isAlive = !isAlive
    }

    fun setCenterOfGravity(x: Double, y: Double) {
        this.centerOfGravityX = x
        this.centerOfGravityY = y
    }
}
