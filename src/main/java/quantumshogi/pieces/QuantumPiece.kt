package quantumshogi.pieces

import javafx.beans.property.SimpleObjectProperty
import quantumshogi.place.Place
import quantumshogi.player.Player

class QuantumPiece(
        val player: Player,
        var place: Place
) {
    val possibles: MutableList<PieceType> = mutableListOf(
            if (player == Player.WHITE) PieceType.KING_HIGHER_RANKED_PLAYER else PieceType.KING_LOWER_RANKED_PLAYER,
            PieceType.ROOK,
            PieceType.BISHOP,
            PieceType.GOLD,
            PieceType.SILVER,
            PieceType.KNIGHT,
            PieceType.LANCE,
            PieceType.PAWN
    )
    val playerProperty = SimpleObjectProperty(player)

    override fun toString(): String {
        return possibles[0].toString()
    }
}