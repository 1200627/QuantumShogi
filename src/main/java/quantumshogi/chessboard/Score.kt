package quantumshogi.chessboard

import quantumshogi.pieces.Piece
import quantumshogi.place.Place2
import quantumshogi.player.Turn

data class Score(
        private val score: List<BoardWithMove> = emptyList()) {
    val board by lazy { score.lastOrNull()?.board ?: BoardModel() }
    val takeBackMoveIfPossible by lazy { Score(score.dropLast(1)) }

    fun move(board: BoardModel, move: MoveOrDrop) = Score(score + BoardWithMove(move, board)).apply { println(BoardWithMove(move, board)) }

    override fun toString(): String {
        return score.joinToString("\n") { it.board.toString() }
    }

    data class BoardWithMove(
            val move: MoveOrDrop,
            val board: BoardModel) {

        override fun toString() = move.pieceBeforeMove.owner.char + "" +
                move.pieceBeforeMove.initialOwner.char +
                "${9 - (move.pieceAfterMove.place as Place2.OnBoard).file}" +
                when (move.pieceAfterMove.place.rank) {
                    0 -> "一"
                    1 -> "二"
                    2 -> "三"
                    3 -> "四"
                    4 -> "五"
                    5 -> "六"
                    6 -> "七"
                    7 -> "八"
                    8 -> "九"
                    else -> throw IllegalStateException()
                } +
                move.pieceAfterMove.possibles.joinToString("") { it.alternateForm } +
                if (move.pieceBeforeMove.place is Place2.InHand) {
                    "打"
                } else {
                    ""
                } +
                if (move.pieceBeforeMove.place is Place2.OnBoard) {
                    when (move.pieceBeforeMove.place.file - move.pieceAfterMove.place.file) {
                        0 -> ""
                        in 1..8 -> if (move.pieceBeforeMove.owner == Turn.BLACK) "右" else "左"
                        in -8..-1 -> if (move.pieceBeforeMove.owner == Turn.BLACK) "左" else "右"
                        else -> throw IllegalStateException()
                    }
                } else {
                    ""
                } +
                if (move.pieceBeforeMove.place is Place2.OnBoard) {
                    when (move.pieceBeforeMove.place.rank - move.pieceAfterMove.place.rank) {
                        0 -> "寄"
                        in 1..8 -> if (move.pieceBeforeMove.owner == Turn.BLACK) "引" else "上"
                        in -8..-1 -> if (move.pieceBeforeMove.owner == Turn.BLACK) "上" else "引"
                        else -> throw IllegalStateException()
                    }
                } else {
                    ""
                }
    }

    data class MoveOrDrop(
            val pieceBeforeMove: Piece,
            val pieceAfterMove: Piece)


}
