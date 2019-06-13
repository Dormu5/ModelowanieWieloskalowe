package visualization

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    @Throws(Exception::class)
    override fun start(primaryStage: Stage) {
        val root = FXMLLoader.load<Parent>(javaClass.getResource("sample.fxml"))
        primaryStage.scene = Scene(root, 1200.0, 700.0)
        primaryStage.sizeToScene()
        primaryStage.isResizable = false
        primaryStage.show()
    }
}
