package quantumshogi.pieces

import quantumshogi.place.Place
import quantumshogi.player.Player

interface Piece {
    var place: Place
    val type: PieceType
    val player: Player
    val possibles: List<PieceType>
}