package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.Piece
import quantumshogi.place.Place2

object Chessboard {
    private val boardView = BoardViewModel
    private var score = Score()

    // Optional : 反則 / 禁じ手 / チェックメイト

    private var selected: Piece? = null

    fun initialize() {
        boardView.updateView(score.board)
    }

    fun takeBackMoveIfPossible() {
        score = score.takeBackMoveIfPossible
        boardView.updateView(score.board)
        boardView.updateHands(score.board)
        boardView.updateScore(score)
    }

    fun moveToIfPossible(to: Place2.OnBoard): Boolean {
        if (selected == null) {
            return false
        }
        val (board, move) = score.board.moveToIfPossible(selected!!, to) ?: return false

        score = score.move(board, move)

        selected = null
        boardView.clearEnterable()
        boardView.updateView(score.board)
        boardView.updateHands(score.board)
        boardView.updateScore(score)

        return true
    }

    fun selectPiece(piece: Piece) {
        if (!score.board.turnIs(piece.owner)) {
            return
        }

        selected = piece
        boardView.clearEnterable()
        boardView.showEnterable(score.board.movements(piece).toSet())

        boardView.updateView(score.board)
    }

    fun clearSelect() {
        selected = null
        boardView.clearEnterable()
        boardView.updateView(score.board)
    }

    fun confirmPromote() = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
        title = "確認"
    }.showAndWait().orElse(ButtonType.NO) == ButtonType.YES
}