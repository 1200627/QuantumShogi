package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.Piece
import quantumshogi.place.Place2

object Chessboard {
    private val boardView = BoardViewModel
    private val history = mutableListOf(BoardModel())

    init {
        boardView.updateView(history.last())
    }

    // 今と最初の状態を見た目が変わるようにする
    // 最初の盤面が表示されるようにする
    // 他の駒を考慮した条件削減
    // Optional : Log / 手戻り / 反則 / 禁じ手 / チェックメイト

    private var selected: Piece? = null

    fun initialize() {
        boardView.updateView(history.last())
    }

    fun takeBackMove() {
        history.removeAt(history.lastIndex)
    }

    fun moveToIfPossible(to: Place2.OnBoard): Boolean {
        if (selected == null) {
            return false
        }
        val next = history.last().moveToIfPossible(selected!!, to)
        if (history.last() == next) {
            return false
        }
        history.add(next)
        println(history.last())

        selected = null
        boardView.clearEnterable()
        boardView.updateView(history.last())
        boardView.updateHands(history.last())

        return true
    }

    fun selectPiece(piece: Piece) {
        if (!history.last().turnIs(piece.owner)) {
            return
        }

        selected = piece
        boardView.clearEnterable()
        boardView.showEnterable(history.last().movements(piece).toSet())

        boardView.updateView(history.last())
    }

    fun clearSelect() {
        selected = null
        boardView.clearEnterable()
        boardView.updateView(history.last())
    }

    fun confirmPromote() = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
        title = "確認"
    }.showAndWait().orElse(ButtonType.NO) == ButtonType.YES
}