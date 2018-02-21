package quantumshogi.places

import quantumshogi.player.Movement

data class OnBoard(val rank: Int, val file: Int) : Place {
    companion object {
        val values by lazy { (0..8).flatMap { rank -> (0..8).map { file -> OnBoard(rank, file) } } }
    }

    operator fun plus(other: Movement): OnBoard {
        return OnBoard(rank + other.forward, file + other.left)
    }

    val toInt by lazy { rank * 9 + file }

    override val isOnBoard by lazy { rank in 0..8 && file in 0..8 }
}