package quantumshogi.place

enum class File(val fromLeft: Int) {
    NINTH(0), EIGHTH(1), SEVENTH(2), SIXTH(3), FIFTH(4), FOURTH(5), THIRD(6), SECOND(7), FIRST(8);

    companion object {
        val reverse = values().associate { it.fromLeft to it }

        fun of(fromLeft: Int) = reverse[fromLeft]
    }

    operator fun plus(other: File): File? {
        return reverse[fromLeft + other.fromLeft]
    }
}