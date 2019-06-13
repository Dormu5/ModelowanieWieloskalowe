package visualization

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty

class Cell(centerOfGravityX: Double, centerOfGravityY: Double) {

    var id: Int = 0
    private val alive = SimpleBooleanProperty(false)
    var grainNumber = 0
    var centerOfGravityX: Double = 0.toDouble()
        private set
    var centerOfGravityY: Double = 0.toDouble()
        private set
    var energy = 0.0
    var isChanged = false
    var density: Double = 0.toDouble()
    var isCrystalized = false

    var isAlive: Boolean
        get() = alive.get()
        set(alive) = this.alive.set(alive)

    init {
        this.centerOfGravityX = centerOfGravityX
        this.centerOfGravityY = centerOfGravityY
    }

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

    fun addDensity(density: Double) {
        this.density += density
    }
}

