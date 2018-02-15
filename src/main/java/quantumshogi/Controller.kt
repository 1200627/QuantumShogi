package quantumshogi

import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.HPos
import javafx.scene.layout.GridPane
import quantumshogi.chessboard.Chessboard
import quantumshogi.chessboard.Place
import java.net.URL
import java.util.*

class Controller : Initializable {
    @FXML
    private lateinit var chessboardPane: GridPane

    override fun initialize(location: URL, resources: ResourceBundle?) {
        (0..8).forEach { y ->
            chessboardPane.columnConstraints[y].halignment = HPos.CENTER
            (0..8).forEach { x ->
                val place = Place(y, x)
                chessboardPane.add(Chessboard[place], x, y)
            }
        }
    }

    @FXML
    fun onReset() {

    }
}
