package quantumshogi.hand

import quantumshogi.chessboard.Chessboard
import quantumshogi.pieces.QuantumPiece
import quantumshogi.player.Player

data class HumanPlayer(private val hand: Hand = Hand(), val turn: Player) {
    operator fun plus(piece: QuantumPiece) = copy(hand = hand + piece)
    operator fun minus(piece: QuantumPiece) = copy(hand = hand - piece)

    fun updateView() {
        if (turn == Player.BLACK) {
            Chessboard.player1Capture.setAll(hand.pieces)
            return
        }
        Chessboard.player2Capture.setAll(hand.pieces)
    }
}