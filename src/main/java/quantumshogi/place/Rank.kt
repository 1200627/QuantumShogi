package quantumshogi.place

enum class Rank(val fromUp: Int) {
    FIRST(0), SECOND(1), THIRD(2), FOURTH(3), FIFTH(4), SIXTH(5), SEVENTH(6), EIGHTH(7), NINTH(8);

    companion object {
        val reverse = values().associate { it.fromUp to it }

        fun of(fromUp: Int) = reverse[fromUp]
    }

    operator fun plus(other: Rank): Rank? {
        return reverse[fromUp + other.fromUp]
    }
}