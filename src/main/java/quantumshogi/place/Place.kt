package quantumshogi.place

import quantumshogi.player.Movement

data class Place(val rank: Int, val file: Int) {
    operator fun plus(other: Movement): Place {
        return Place(rank + other.forward, file + other.left)
    }


    val isOnBoard by lazy { rank in 0..8 && file in 0..8 }
}