package quantumshogi.pieces

import javafx.beans.property.SimpleObjectProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import quantumshogi.place.Place
import quantumshogi.player.Player

class QuantumPiece(
        override val player: Player,
        override var place: Place
) : Piece {
    override val possibles: ObservableList<PieceType> = FXCollections.observableArrayList(
            //if (player == Player.BLACK) PieceType.KING_HIGHER_RANKED_PLAYER else PieceType.KING_LOWER_RANKED_PLAYER,
            //PieceType.ROOK,
            PieceType.BISHOP,
            //PieceType.GOLD,
            //PieceType.SILVER,
            //PieceType.KNIGHT,
            //PieceType.LANCE,
            PieceType.PAWN
    )
    override val playerProperty = SimpleObjectProperty(player)

    override fun toString(): String {
        return possibles[0].toString()
    }
}