package quantumshogi.player

enum class Turn(val color: String, val char: Char) {
    BLACK("#000000", '▲') {
        override val promotableRank = 6..8

        override val oneSquareStraightForward by lazy { Movement(1, 0) }

        override val next by lazy { WHITE }
    },

    WHITE("#FFFFFF", '△') {
        override val promotableRank = 0..2

        override val oneSquareStraightForward by lazy { Movement(-1, 0) }

        override val next by lazy { BLACK }
    };

    abstract val promotableRank: IntRange
    abstract val oneSquareStraightForward: Movement

    val oneSquareInAnyDirection by lazy {
        oneSquareOrthogonally + oneSquareDiagonally
    }

    val oneSquareOrthogonally by lazy {
        setOf(Movement(1, 0),
                Movement(-1, 0),
                Movement(0, 1),
                Movement(0, -1))
    }

    val oneSquareDiagonally by lazy { oneSquareDiagonallyForward + oneSquareDiagonallyBackward }

    val oneSquareDiagonallyForward by lazy {
        setOf(Movement(0, 1) + oneSquareStraightForward,
                Movement(0, -1) + oneSquareStraightForward)
    }

    private val oneSquareDiagonallyBackward by lazy {
        setOf(Movement(0, 1) - oneSquareStraightForward,
                Movement(0, -1) - oneSquareStraightForward)
    }

    abstract val next: Turn
}