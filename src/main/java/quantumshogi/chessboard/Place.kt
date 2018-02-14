package quantumshogi.chessboard

import quantumshogi.player.Movement

data class Place(val rank: Int, val file: Int) {
    operator fun plus(other: Movement): Place {
        return Place(rank + other.forward, file + other.left)
    }
}