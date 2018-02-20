package quantumshogi.place

import quantumshogi.player.Movement
import quantumshogi.player.Turn

interface Place2 {

    val isOnBoard: Boolean

    data class OnBoard(val rank: Int, val file: Int) : Place2 {
        companion object {
            val values by lazy { (0..8).flatMap { rank -> (0..8).map { file -> OnBoard(rank, file) } } }
        }

        operator fun plus(other: Movement): OnBoard {
            return OnBoard(rank + other.forward, file + other.left)
        }

        val toInt by lazy { rank * 9 + file }

        override val isOnBoard by lazy { rank in 0..8 && file in 0..8 }
    }

    enum class InHand : Place2 {
        BLACK, WHITE;

        companion object {
            fun of(turn: Turn): InHand = when (turn) {
                Turn.BLACK -> BLACK
                Turn.WHITE -> WHITE
            }
        }

        override val isOnBoard by lazy { false }
    }
}