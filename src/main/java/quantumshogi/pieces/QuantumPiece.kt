package quantumshogi.pieces

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import quantumshogi.chessboard.Chessboard
import quantumshogi.chessboard.Place
import quantumshogi.player.Player

class QuantumPiece(
        override val player: Player,
        override val place: Place
) : Rectangle(30.0, 40.0, Color.valueOf(player.color)), Piece {
    override val type by lazy { possibles[0] }

    override val possibles: List<PieceType> = PieceType.values().toMutableList()

    init {
        stroke = Color.BLACK
        strokeWidth = 1.0
        setOnMouseClicked {
            if (!Chessboard.turnIs(player)) {
                return@setOnMouseClicked
            }

            Chessboard.selectPiece(place, player)
        }
    }

    override fun toString(): String {
        return possibles[0].toString()
    }
}