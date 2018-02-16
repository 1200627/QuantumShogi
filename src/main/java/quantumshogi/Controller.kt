package quantumshogi

import javafx.beans.binding.Bindings
import javafx.beans.binding.BooleanBinding
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import quantumshogi.chessboard.Chessboard
import quantumshogi.pieces.PieceType
import quantumshogi.place.Place
import java.net.URL
import java.util.*
import java.util.concurrent.Callable

class Controller : Initializable {
    @FXML
    private lateinit var chessboardPane: GridPane

    override fun initialize(location: URL, resources: ResourceBundle?) {
        (0..8).forEach { y ->
            chessboardPane.columnConstraints[y].halignment = HPos.CENTER
            (0..8).forEach { x ->
                val place = Place(y, x)
                val square by lazy { Chessboard.boardView[place] }
                val stackPane = StackPane().apply {
                    alignment = Pos.CENTER
                    styleProperty().bind(Bindings.createStringBinding(Callable{
                        if (square.enterableProperty.value) {
                            return@Callable "-fx-background-color:#ff0000a0"
                        }
                        ""
                    }, square.enterableProperty))
                    setOnMouseClicked {
                        if (square.piece != null) {
                            Chessboard.selectPiece(place)
                        }
                        Chessboard.moveToIfPossible(place)
                    }

                    children.add(Rectangle(30.0, 40.0).apply {
                        stroke = Color.BLACK
                        strokeWidth = 1.0
                        visibleProperty().bind(square.hasPieceProperty)
                        disableProperty().bind(!square.hasPieceProperty)
                        fillProperty().bind(Bindings.createObjectBinding(Callable {
                            Color.valueOf(square.piece?.player?.color ?: "#ffffff")
                        }, square.pieceProperty))
                    })

                    PieceType.values().forEach {
                        val text = Text(it.toString()).apply {
                            fill = Color.SKYBLUE
                            visibleProperty().bind(Bindings.createBooleanBinding(Callable {
                                square.piece?.possibles?.contains(it) ?: false
                            }, square.pieceProperty))
                        }
                        children.add(text)
                    }
                }
                chessboardPane.add(stackPane, x, y)
            }
        }
    }

    @FXML
    fun onReset() {

    }
}
