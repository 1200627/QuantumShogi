package quantumshogi.player

enum class Turn(val color: String, val char: String) {
    BLACK("#000000", "☗") {
        override val promotableRank = 6..8

        override val forward by lazy { Movement(1, 0) }
        override val backward by lazy { Movement(-1, 0) }
        override val left by lazy { Movement(0, 1) }
        override val right by lazy { Movement(0, -1) }
        override val leftForward by lazy { Movement(1, 1) }
        override val rightForward by lazy { Movement(1, -1) }
        override val leftBackward by lazy { Movement(-1, 1) }
        override val rightBackward by lazy { Movement(-1, -1) }

        override val next by lazy { WHITE }
    },

    WHITE("#FFFFFF", "☖") {
        override val promotableRank = 0..2

        override val forward by lazy { Movement(-1, 0) }
        override val backward by lazy { Movement(1, 0) }
        override val left by lazy { Movement(0, 1) }
        override val right by lazy { Movement(0, -1) }
        override val leftForward by lazy { Movement(-1, 1) }
        override val rightForward by lazy { Movement(-1, -1) }
        override val leftBackward by lazy { Movement(1, 1) }
        override val rightBackward by lazy { Movement(1, -1) }

        override val next by lazy { BLACK }
    };

    abstract val promotableRank: IntRange
    abstract val forward: Movement
    abstract val backward: Movement
    abstract val left: Movement
    abstract val right: Movement
    abstract val leftForward: Movement
    abstract val rightForward: Movement
    abstract val leftBackward: Movement
    abstract val rightBackward: Movement

    abstract val next: Turn
}