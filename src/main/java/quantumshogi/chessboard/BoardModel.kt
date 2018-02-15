package quantumshogi.chessboard

import quantumshogi.pieces.Piece

data class BoardModel(private val pieces: Map<Place, Piece>) {
    constructor(pieces: Set<Piece>) : this(pieces.associate { it.place to it })

    operator fun get(place: Place) = pieces[place]

    override fun toString() = (0..8).joinToString(separator = "") { rank ->
        (0..8).joinToString(separator = "", postfix = LINE_SEPARATOR) { file ->
            pieces[Place(rank, file)]?.toString() ?: "□"
        }
    }

    companion object {
        val LINE_SEPARATOR = System.getProperty("line.separator")!!
    }
}