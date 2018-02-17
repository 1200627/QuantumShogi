package quantumshogi.pieces

import javafx.beans.property.SimpleObjectProperty
import quantumshogi.place.Place
import quantumshogi.player.Turn

data class QuantumPiece(
        val player: Turn,
        val place: Place,
        val possibles: List<PieceType> = init(player)
) {
    companion object {
        fun init(player: Turn) = listOf(
                if (player == Turn.WHITE) PieceType.KING_HIGHER_RANKED_PLAYER else PieceType.KING_LOWER_RANKED_PLAYER,
                PieceType.ROOK,
                PieceType.BISHOP,
                PieceType.GOLD,
                PieceType.SILVER,
                PieceType.KNIGHT,
                PieceType.LANCE,
                PieceType.PAWN
        )
    }

    val playerProperty = SimpleObjectProperty(player)

}