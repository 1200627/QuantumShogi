package quantumshogi.pieces

import quantumshogi.chessboard.Place
import quantumshogi.player.Player

interface Piece {
    val place: Place
    val type: PieceType
    val player: Player
}