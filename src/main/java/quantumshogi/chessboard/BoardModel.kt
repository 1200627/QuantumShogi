package quantumshogi.chessboard

import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place

data class BoardModel(private val pieces: Map<Place, QuantumPiece>) {
    constructor(pieces: Set<QuantumPiece>) : this(pieces.associate { it.place to it })

    operator fun get(place: Place) = pieces[place]

    override fun toString() = (0..8).joinToString(separator = "") { rank ->
        (0..8).joinToString(separator = "", postfix = LINE_SEPARATOR) { file ->
            pieces[Place(rank, file)]?.player?.char ?: "□"
        }
    }

    companion object {
        val LINE_SEPARATOR = System.getProperty("line.separator")!!
    }
}