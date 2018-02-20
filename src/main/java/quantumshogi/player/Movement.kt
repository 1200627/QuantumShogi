package quantumshogi.player

data class Movement(val forward: Int, val left: Int) {
    operator fun plus(other: Movement) = Movement(forward + other.forward, left + other.left)
    operator fun minus(other: Movement) = Movement(forward - other.forward, left - other.left)
}