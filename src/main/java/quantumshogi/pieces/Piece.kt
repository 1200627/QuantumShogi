package quantumshogi.pieces

import quantumshogi.chessboard.Place
import quantumshogi.player.Player

interface Piece {
    var place: Place
    val type: PieceType
    val player: Player
    val possibles: List<PieceType>
}