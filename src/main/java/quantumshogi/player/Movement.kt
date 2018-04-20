package quantumshogi.player

data class Movement(val forward: Int, val left: Int) {
    operator fun plus(other: Movement) = Movement(forward + other.forward, left + other.left)
    operator fun minus(other: Movement) = Movement(forward - other.forward, left - other.left)

    companion object {
        val ONE_SQUARE_ORTHOGONALLY by lazy {
            setOf(Movement(1, 0),
                    Movement(-1, 0),
                    Movement(0, 1),
                    Movement(0, -1))
        }

        val ONE_SQUARE_DIAGONALLY by lazy {
            setOf(Movement(1, 1),
                    Movement(1, -1),
                    Movement(-1, 1),
                    Movement(-1, -1))
        }

        val ONE_SQUARE_IN_ANY_DIRECTION by lazy {
            ONE_SQUARE_ORTHOGONALLY + ONE_SQUARE_DIAGONALLY
        }
    }
}