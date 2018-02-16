package quantumshogi.hand

import quantumshogi.pieces.QuantumPiece

data class Hand(val pieces: Set<QuantumPiece> = emptySet()) {
    operator fun plus(piece: QuantumPiece) = Hand(pieces + piece)
    operator fun minus(piece: QuantumPiece) = Hand(pieces - piece)
}