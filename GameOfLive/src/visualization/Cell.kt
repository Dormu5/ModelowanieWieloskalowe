package visualization

import javafx.beans.property.BooleanProperty
import javafx.beans.property.SimpleBooleanProperty


class Cell {

    private val alive = SimpleBooleanProperty(false)

    var isAlive: Boolean
        get() = alive.get()
        set(alive) = this.alive.set(alive)

    fun aliveProperty(): BooleanProperty {
        return alive
    }

    fun negateAlive() {
        isAlive = !isAlive
    }
}
