package quantumshogi.pieces

import quantumshogi.place.Place2
import quantumshogi.player.Turn
import java.util.*

data class Piece(
        val initialOwner: Turn,
        val owner: Turn = initialOwner,
        val place: Place2,
        val possibles: List<PieceType> = init(initialOwner),
        val id: UUID = UUID.randomUUID()) {
    companion object {
        fun init(player: Turn) = listOf(
                if (player == Turn.WHITE) {
                    PieceType.KING_HIGHER_RANKED_PLAYER
                } else {
                    PieceType.KING_LOWER_RANKED_PLAYER
                },
                PieceType.ROOK,
                PieceType.BISHOP,
                PieceType.GOLD,
                PieceType.SILVER,
                PieceType.KNIGHT,
                PieceType.LANCE,
                PieceType.PAWN
        )
    }
}