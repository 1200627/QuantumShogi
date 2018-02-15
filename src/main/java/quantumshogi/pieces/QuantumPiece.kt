package quantumshogi.pieces

import javafx.beans.property.SimpleObjectProperty
import quantumshogi.place.Place
import quantumshogi.player.Player

class QuantumPiece(
        override val player: Player,
        override var place: Place
) : Piece {
    override val type by lazy { possibles[0] }
    override val possibles: List<PieceType> = PieceType.values().toMutableList()
    override val playerProperty = SimpleObjectProperty(player)

    override fun toString(): String {
        return possibles[0].toString()
    }
}