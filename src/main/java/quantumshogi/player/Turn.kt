package quantumshogi.player

enum class Turn(val color: String, val char: Char, val promotableRank: IntRange, val oneSquareStraightForward: Movement) {
    BLACK("#000000", '▲', 6..8, Movement(1, 0)) {
        override val next by lazy { WHITE }
    },

    WHITE("#FFFFFF", '△', 0..2, Movement(-1, 0)) {
        override val next by lazy { BLACK }
    };

    val oneSquareDiagonallyForward by lazy {
        setOf(Movement(0, 1) + oneSquareStraightForward,
                Movement(0, -1) + oneSquareStraightForward)
    }

    abstract val next: Turn
}