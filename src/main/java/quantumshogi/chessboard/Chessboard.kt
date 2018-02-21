package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.Piece
import quantumshogi.place.Place2

object Chessboard {
    private val boardView = BoardViewModel
    private var boardModel = BoardModel()

    init {
        boardView.updateView(boardModel)
    }

    // 今と最初の状態を見た目が変わるようにする
    // 最初の盤面が表示されるようにする
    // 他の駒を考慮した条件削減
    // Optional : Log / 手戻り / 反則 / 禁じ手 / チェックメイト

    private var selected: Piece? = null

    fun initialize() {
        boardView.updateView(boardModel)
    }

    fun moveToIfPossible(to: Place2.OnBoard): Boolean {
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
        boardView.updateHands(boardModel)

        return true
    }

    fun selectPiece(piece: Piece) {
        if (!boardModel.turnIs(piece.owner)) {
            return
        }

        selected = piece
        boardView.clearEnterable()
        boardView.showEnterable(boardModel.movements(piece).toSet())

        boardView.updateView(boardModel)
    }

    fun clearSelect() {
        selected = null
        boardView.clearEnterable()
        boardView.updateView(boardModel)
    }

    fun confirmPromote() = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
        title = "確認"
    }.showAndWait().orElse(ButtonType.NO) == ButtonType.YES
}