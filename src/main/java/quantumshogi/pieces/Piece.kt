package quantumshogi.pieces

import javafx.beans.property.SimpleObjectProperty
import quantumshogi.place.Place
import quantumshogi.player.Player

interface Piece {
    var place: Place
    val type: PieceType
    val player: Player
    val playerProperty: SimpleObjectProperty<Player>
    val possibles: List<PieceType>
}