package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.PieceType
import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place
import quantumshogi.player.Player

object Chessboard {
    val boardView = BoardViewModel()
    private var boardModel = BoardModel()

    init {
        boardView.updateView(boardModel)
    }

    private var selected: Place? = null

    fun moveToIfPossible(to: Place): Boolean {
        if (selected == null) {
            return false
        }
        val old = boardModel
        boardModel = boardModel.moveToIfPossible(selected!!, to)
        if (old == boardModel) {
            return false
        }
        println(boardModel)

        selected = null
        boardView.clearEnterable()
        boardView.updateView(boardModel)

        return true
    }

    fun selectPiece(place: Place) {
        val player = boardModel.playing
        if (boardModel[place]?.player != player) {
            return
        }

        selected = place
        boardView.clearEnterable()
        boardView.showEnterable(boardModel.movements(place).toSet())

        boardView.updateView(boardModel)
    }

    fun confirmPromote(): Boolean {
        val alert = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
            title = "確認"
        }
        val selected = alert.showAndWait().orElse(ButtonType.NO)
        return selected == ButtonType.YES
    }
}