package quantumshogi

import javafx.beans.binding.Bindings
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import javafx.fxml.FXML
import javafx.fxml.Initializable
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.control.ListView
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.text.Text
import javafx.util.Callback
import quantumshogi.chessboard.BoardViewModel
import quantumshogi.chessboard.Chessboard
import quantumshogi.components.PieceCell
import quantumshogi.pieces.Piece
import quantumshogi.pieces.PieceType
import quantumshogi.place.Place2
import quantumshogi.player.Turn
import java.net.URL
import java.util.*
import java.util.concurrent.Callable


class Controller : Initializable {
    @FXML
    private lateinit var chessboardPane: GridPane

    @FXML
    private lateinit var player1CaptureView: ListView<Piece>
    @FXML
    private lateinit var player2CaptureView: ListView<Piece>
    @FXML
    private lateinit var selectedView: ListView<Piece>

    private val selectedList: ObservableList<Piece> = FXCollections.observableArrayList()

    override fun initialize(location: URL, resources: ResourceBundle?) {
        BoardViewModel.bindBlackHand(player1CaptureView::setItems)
        BoardViewModel.bindWhiteHand(player2CaptureView::setItems)
        player1CaptureView.cellFactory = Callback { _ -> PieceCell() }
        player2CaptureView.cellFactory = Callback { _ -> PieceCell() }
        selectedView.items = selectedList
        selectedView.cellFactory = Callback { _ -> PieceCell() }

        player1CaptureView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                Chessboard.selectPiece(newValue)
            }
        }
        player2CaptureView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            if (newValue != null) {
                Chessboard.selectPiece(newValue)
            }
        }

        // Initialization of GridPane
        (0..8).forEach { y ->
            chessboardPane.columnConstraints[y].halignment = HPos.CENTER
            (0..8).forEach { x ->
                val place = Place2.OnBoard(y, x)
                val square = BoardViewModel.get(place)
                val stackPane = StackPane().apply {
                    alignment = Pos.CENTER
                    styleProperty().bind(Bindings.createStringBinding(Callable {
                        if (square.enterableProperty.value) {
                            return@Callable "-fx-background-color:#ff0000a0"
                        }
                        ""
                    }, square.enterableProperty))
                    setOnMouseClicked {
                        player1CaptureView.selectionModel.clearSelection()
                        player2CaptureView.selectionModel.clearSelection()
                        if (square.piece != null) {
                            selectedList.setAll(square.piece!!)
                            Chessboard.selectPiece(square.piece!!)
                        } else if (!square.enterableProperty.value) {
                            Chessboard.clearSelect()
                        }
                        Chessboard.moveToIfPossible(place)
                    }

                    children.add(Polygon(
                            0.0, 20.0,
                            15.0, 10.0,
                            15.0, -20.0,
                            -15.0, -20.0,
                            -15.0, 10.0,
                            0.0, 20.0
                    ).apply {
                        stroke = Color.BLACK
                        strokeWidth = 1.0
                        visibleProperty().bind(square.hasPieceProperty)
                        disableProperty().bind(!square.hasPieceProperty)
                        fillProperty().bind(Bindings.createObjectBinding(Callable {
                            Color.valueOf(square.piece?.initialOwner?.color ?: "#ffffff")
                        }, square.pieceProperty))
                        rotateProperty().bind(Bindings.createDoubleBinding(Callable {
                            if (square.piece?.owner == Turn.BLACK) {
                                0.0
                            } else {
                                180.0
                            }
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

        Chessboard.initialize()
    }

    @FXML
    fun onReset() {

    }
}
