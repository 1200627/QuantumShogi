package quantumshogi.chessboard

import quantumshogi.pieces.Piece
import quantumshogi.pieces.QuantumPiece

data class BoardModel(private val pieces: Map<Place, Piece>) {
    operator fun get(place: Place) = pieces[place]

    override fun toString(): String {
        val builder = StringBuilder(81)
        (0..8).forEach { rank ->
            (0..8).forEach { file ->
                builder.append(pieces[Place(rank, file)]?.toString() ?: "□")
            }
            builder.appendln()
        }
        return builder.toString()
    }

    fun toSquare(): Set<Square> {
        val set = mutableSetOf<Square>()

        (0..8).forEach { rank ->
            (0..8).forEach { file ->
                val old = pieces[Place(rank, file)]
                if (old == null) {
                    set.add(Square(null, file, rank))
                } else {
                    set.add(Square(QuantumPiece(old.player, file, rank), file, rank))
                }
            }
        }

        return set
    }
}