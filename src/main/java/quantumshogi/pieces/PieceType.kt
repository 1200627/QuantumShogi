package quantumshogi.pieces

import quantumshogi.chessboard.Place
import quantumshogi.player.Player
import kotlin.math.abs

enum internal class PieceType(private val string: String) {
    FUHYO("歩兵") {
        override fun movements(place: Place, player: Player): Set<Place> {
            if (place.rank + player.direction in 0..8) {
                return setOf(Place(place.rank + player.direction, place.file))
            }
            return setOf()
        }
    },
    HISHA("飛車") {
        override fun movements(place: Place, player: Player): Set<Place> {
            val list1 = (0 until 9).filter { it != place.rank }.map {
                Place(it, place.file)
            }
            val list2 = (0 until 9).filter { it != place.file }.map {
                Place(place.rank, it)
            }
            return (list1 + list2).toSet()
        }
    },
    KAKUGYO("角行") {
        override fun movements(place: Place, player: Player): Set<Place> {
            TODO("Bug fix Needed")
            return (0 until 9).filter { it != place.file }.flatMap {
                val diff = abs(place.rank - it)
                listOf(Place(it, place.file - diff), Place(it, place.file + diff))
            }.toSet()
        }
    },
    KYOSHA("香車") {
        override fun movements(place: Place, player: Player): Set<Place> {
            return (1..9).map { place.rank + it * player.direction }.takeWhile { it in 0..9 }.map {
                Place(it, place.file)
            }.toSet()
        }
    },
    KEIMA("桂馬") {
        override fun movements(place: Place, player: Player): Set<Place> {
            return setOf(
                    Place(place.rank + 2 * player.direction, place.file + 1),
                    Place(place.rank + 2 * player.direction, place.file - 1)
            )
        }
    },
    GIN("銀将") {
        override fun movements(place: Place, player: Player): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file),
                    Place(place.rank + 1 * player.direction, place.file - 1),
                    Place(place.rank + 1 * player.direction, place.file + 1),
                    Place(place.rank - 1 * player.direction, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file + 1)
            )
        }
    },
    KIN("金将") {
        override fun movements(place: Place, player: Player): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file),
                    Place(place.rank + 1 * player.direction, place.file - 1),
                    Place(place.rank + 1 * player.direction, place.file + 1),
                    Place(place.rank, place.file + 1),
                    Place(place.rank, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file)
            )
        }
    },
    OU("王将") {
        override fun movements(place: Place, player: Player): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file),
                    Place(place.rank + 1 * player.direction, place.file - 1),
                    Place(place.rank + 1 * player.direction, place.file + 1),
                    Place(place.rank, place.file + 1),
                    Place(place.rank, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file),
                    Place(place.rank - 1 * player.direction, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file + 1)
            )
        }
    };

    abstract fun movements(place: Place, player: Player): Set<Place>

    override fun toString() = string
}
