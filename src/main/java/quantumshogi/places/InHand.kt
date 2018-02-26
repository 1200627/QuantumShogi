package quantumshogi.places

import quantumshogi.player.Turn

enum class InHand : Place {
    BLACK, WHITE;

    companion object {
        fun of(turn: Turn): InHand = when (turn) {
            Turn.BLACK -> BLACK
            Turn.WHITE -> WHITE
        }
    }

    override val isOnBoard by lazy { false }
}