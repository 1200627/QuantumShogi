package quantumshogi.pieces

import javafx.beans.property.SimpleObjectProperty
import quantumshogi.place.Place
import quantumshogi.player.Player

data class QuantumPiece(
        val player: Player,
        val place: Place,
        val possibles: List<PieceType> = init(player)
) {
    companion object {
        fun init(player: Player) = listOf(
                if (player == Player.WHITE) PieceType.KING_HIGHER_RANKED_PLAYER else PieceType.KING_LOWER_RANKED_PLAYER,
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