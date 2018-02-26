package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.Piece
import quantumshogi.places.OnBoard

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

    fun moveToIfPossible(to: OnBoard): Boolean {
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

        selected = piece
        boardView.clearEnterable()
        boardView.showEnterable(score.board.movements(piece).toSet())
    }

    fun clearSelect() {
        selected = null
        boardView.clearEnterable()
    }

    fun confirmPromote() = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
        title = "確認"
    }.showAndWait().orElse(ButtonType.NO) == ButtonType.YES
}