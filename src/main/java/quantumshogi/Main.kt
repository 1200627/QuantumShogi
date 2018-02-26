package quantumshogi

import javafx.application.Application
import javafx.fxml.FXMLLoader
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.stage.Stage

class Main : Application() {
    override fun start(primaryStage: Stage) = primaryStage.apply {
        title = "量子将棋γ"
        scene = Scene(FXMLLoader.load<Parent>(this@Main.javaClass.getResource("/main.fxml")))
    }.show()
}