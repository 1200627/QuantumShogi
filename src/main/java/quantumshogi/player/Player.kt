package quantumshogi.player

enum class Player(val direction: Int, val color: String, val char: String) {
    BLACK(1, "#000000", "☗") {
        override val forward by lazy { Movement(1, 0) }
        override val backward by lazy { Movement(-1, 0) }
        override val left by lazy { Movement(0, 1) }
        override val right by lazy { Movement(0, -1) }
        override val leftForward by lazy { Movement(1, 1) }
        override val rightForward by lazy { Movement(1, -1) }
        override val leftBackward by lazy { Movement(-1, 1) }
        override val rightBackward by lazy { Movement(-1, -1) }

        override val nextPlayer by lazy { WHITE }
    },

    WHITE(-1, "#FFFFFF", "☖") {
        override val forward by lazy { Movement(-1, 0) }
        override val backward by lazy { Movement(1, 0) }
        override val left by lazy { Movement(0, 1) }
        override val right by lazy { Movement(0, -1) }
        override val leftForward by lazy { Movement(-1, 1) }
        override val rightForward by lazy { Movement(-1, -1) }
        override val leftBackward by lazy { Movement(1, 1) }
        override val rightBackward by lazy { Movement(1, -1) }

        override val nextPlayer by lazy { BLACK }
    };


    abstract val forward: Movement
    abstract val backward: Movement
    abstract val left: Movement
    abstract val right: Movement
    abstract val leftForward: Movement
    abstract val rightForward: Movement
    abstract val leftBackward: Movement
    abstract val rightBackward: Movement

    abstract val nextPlayer: Player
}