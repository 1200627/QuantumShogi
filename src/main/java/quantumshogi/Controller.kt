package quantumshogi

import javafx.beans.binding.Bindings
import javafx.fxml.FXML
import javafx.fxml.FXMLLoader
import javafx.fxml.Initializable
import javafx.geometry.HPos
import javafx.geometry.Pos
import javafx.scene.Parent
import javafx.scene.Scene
import javafx.scene.control.ListView
import javafx.scene.control.TextArea
import javafx.scene.layout.GridPane
import javafx.scene.layout.StackPane
import javafx.stage.Stage
import javafx.util.Callback
import quantumshogi.chessboard.BoardViewModel
import quantumshogi.chessboard.Chessboard
import quantumshogi.components.PieceBox
import quantumshogi.components.PieceCell
import quantumshogi.components.PiecePolygonPane
import quantumshogi.pieces.Piece
import quantumshogi.places.OnBoard
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
    private lateinit var selectedPane: StackPane
    private val selectedBox = PieceBox()

    @FXML
    private lateinit var logArea: TextArea

    override fun initialize(location: URL, resources: ResourceBundle?) {
        BoardViewModel.bindBlackHand(player1CaptureView::setItems)
        BoardViewModel.bindWhiteHand(player2CaptureView::setItems)
        player1CaptureView.cellFactory = Callback { _ -> PieceCell() }
        player2CaptureView.cellFactory = Callback { _ -> PieceCell() }
        selectedPane.children.add(selectedBox)
        logArea.textProperty().bind(BoardViewModel.scoreSheet)

        player1CaptureView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.apply(Chessboard::selectPiece)
        }
        player2CaptureView.selectionModel.selectedItemProperty().addListener { _, _, newValue ->
            newValue?.apply(Chessboard::selectPiece)
        }

        // Initialization of GridPane
        (0..8).forEach { y ->
            chessboardPane.columnConstraints[y].halignment = HPos.CENTER
            (0..8).forEach { x ->
                val place = OnBoard(y, x)
                val square = BoardViewModel.get(place)
                val stackPane = StackPane().apply {
                    alignment = Pos.CENTER
                    minWidth = 0.0
                    minHeight = 0.0
                    styleProperty().bind(Bindings.createStringBinding(Callable {
                        if (square.enterableProperty.value) {
                            return@Callable "-fx-background-color:#ff0000a0"
                        }
                        ""
                    }, square.enterableProperty))
                    setOnMouseClicked {
                        player1CaptureView.selectionModel.clearSelection()
                        player2CaptureView.selectionModel.clearSelection()
                        selectedBox.updateItem(square.piece)
                        if (Chessboard.moveToIfPossible(place)) {
                            return@setOnMouseClicked
                        }
                        if (square.piece != null) {
                            Chessboard.selectPiece(square.piece!!)
                        } else if (!square.enterableProperty.value) {
                            Chessboard.clearSelect()
                        }

                    }

                    children.add(PiecePolygonPane().apply {
                        pieceProperty.bind(square.pieceProperty)
                        scaleXProperty().bind(chessboardPane.widthProperty().divide(chessboardPane.prefWidthProperty()))
                        scaleYProperty().bind(chessboardPane.heightProperty().divide(chessboardPane.prefHeightProperty()))
                    })
                }
                chessboardPane.add(stackPane, x, y)
            }
        }
        Chessboard.initialize()
    }

    @FXML
    fun onConnect() {
        val fxmlLoader = FXMLLoader(javaClass.getResource("/connect.fxml"))
        val root = fxmlLoader.load<Parent>()
        fxmlLoader.getController<ConnectController>().controller = this
        val scene = Scene(root)
        val stage = Stage().apply {
            this.scene = scene
            title = "接続"
        }
        stage.showAndWait()
    }

    @FXML
    fun onRetract() {
        Chessboard.takeBackMoveIfPossible()
    }

    fun print(x: Any) {
        logArea.appendText(x.toString())
    }

    fun println(x: Any) {
        print("$x\n")
    }
}
