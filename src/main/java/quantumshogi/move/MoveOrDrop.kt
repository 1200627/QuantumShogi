package quantumshogi.move

import quantumshogi.pieces.Piece

data class MoveOrDrop(
        val pieceBeforeMove: Piece,
        val pieceAfterMove: Piece)