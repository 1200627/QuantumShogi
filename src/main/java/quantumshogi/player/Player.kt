package quantumshogi.player

enum class Player(val direction: Int) {
    P1(1) {
        override val nextPlayer by lazy { P2 }
    },

    P2(-1) {
        override val nextPlayer by lazy { P1 }
    };

    abstract val nextPlayer: Player
}