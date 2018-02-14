package quantumshogi.pieces

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import quantumshogi.chessboard.Chessboard
import quantumshogi.chessboard.Place
import quantumshogi.player.Player

class QuantumPiece(
        override val player: Player,
        private var x: Int,
        private var y: Int
) : Rectangle(30.0, 40.0, Color.valueOf(player.color)), Piece {
    override val place by lazy { Place(y, x) }
    override val type by lazy { possibles[0] }

    private val possibles: List<PieceType> = PieceType.values().toMutableList()

    init {
        stroke = Color.BLACK
        strokeWidth = 1.0
        setOnMouseClicked {
            if (!Chessboard.turnIs(player)) {
                return@setOnMouseClicked
            }

            Chessboard.clearStyle()
            val possibleDestination = possibles.flatMap {
                it.movements(Place(y, x), player, Chessboard.toModel()).map { it.file to it.rank }
            }.toSet()

            val checked = possibleDestination.filter {
                val sq = Chessboard.get(it.first, it.second)
                val qp = (sq.piece as? QuantumPiece)
                qp == null || !Chessboard.turnIs(qp.player)
            }

            checked.forEach {
                val square = Chessboard.get(it.first, it.second)
                square.style = "-fx-background-color:#ff0000a0"
            }

            Chessboard.selectPiece()
            Chessboard.movable = checked
            Chessboard.selected = x to y
        }
    }

    override fun toString(): String {
        return possibles[0].toString()
    }
}