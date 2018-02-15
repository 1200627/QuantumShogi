package quantumshogi.chessboard

import quantumshogi.pieces.Piece
import quantumshogi.place.Place
import quantumshogi.player.Player

data class BoardModel(private val pieces: Map<Place, Piece>) {
    constructor(pieces: Set<Piece>) : this(pieces.associate { it.place to it })

    operator fun get(place: Place) = pieces[place]

    override fun toString() = (0..8).joinToString(separator = "") { rank ->
        (0..8).joinToString(separator = "", postfix = LINE_SEPARATOR) { file ->
            when (pieces[Place(rank, file)]?.player) {
                Player.P1 -> "☗"
                Player.P2 -> "☖"
                else -> "□"
            }
        }
    }

    companion object {
        val LINE_SEPARATOR = System.getProperty("line.separator")!!
    }
}