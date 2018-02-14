package quantumshogi.pieces

import quantumshogi.chessboard.BoardModel
import quantumshogi.chessboard.Place
import quantumshogi.player.Player

enum class PieceType(private val string: String) {
    KING("王") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file),
                    Place(place.rank + 1 * player.direction, place.file - 1),
                    Place(place.rank + 1 * player.direction, place.file + 1),
                    Place(place.rank, place.file + 1),
                    Place(place.rank, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file),
                    Place(place.rank - 1 * player.direction, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file + 1)
            ).filter { board[it] == null || board[it]!!.player != player }.toSet()
        }
    },
    ROOK("飛") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            val set = mutableSetOf<Place>()

            listOf(player.forward, player.left, player.backward, player.right).forEach {
                var now = place
                while (true) {
                    now += it
                    if (!now.isOnBoard) {
                        break
                    }
                    val movedRectangle = board[now]
                    if (movedRectangle == null) {
                        set.add(now)
                        continue
                    }
                    if (movedRectangle.player != player) {
                        set.add(now)
                        break
                    }
                    break
                }
            }

            return set
        }
    },
    BISHOP("角") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            val set = mutableSetOf<Place>()

            listOf(player.leftForward, player.leftBackward, player.rightForward, player.rightBackward).forEach {
                var now = place
                while (true) {
                    now += it
                    if (!now.isOnBoard) {
                        break
                    }
                    val movedRectangle = board[now]
                    if (movedRectangle == null) {
                        set.add(now)
                        continue
                    }
                    if (movedRectangle.player != player) {
                        set.add(now)
                        break
                    }
                    break
                }
            }

            return set
        }
    },
    KIN("金") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file),
                    Place(place.rank + 1 * player.direction, place.file - 1),
                    Place(place.rank + 1 * player.direction, place.file + 1),
                    Place(place.rank, place.file + 1),
                    Place(place.rank, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file)
            ).filter { board[it] == null || board[it]!!.player != player }.toSet()
        }
    },
    GIN("銀") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file),
                    Place(place.rank + 1 * player.direction, place.file - 1),
                    Place(place.rank + 1 * player.direction, place.file + 1),
                    Place(place.rank - 1 * player.direction, place.file - 1),
                    Place(place.rank - 1 * player.direction, place.file + 1)
            ).filter { board[it] == null || board[it]!!.player != player }.toSet()
        }
    },
    KEIMA("桂") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return setOf(
                    Place(place.rank + 2 * player.direction, place.file + 1),
                    Place(place.rank + 2 * player.direction, place.file - 1)
            ).filter { board[it] == null || board[it]!!.player != player }.toSet()
        }
    },
    KYOSHA("香") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            val set = mutableSetOf<Place>()

            var now = place
            while (true) {
                now += player.forward
                if (!now.isOnBoard) {
                    break
                }
                val movedRectangle = board[now]
                if (movedRectangle == null) {
                    set.add(now)
                    continue
                }
                if (movedRectangle.player != player) {
                    set.add(now)
                    break
                }
                break
            }

            return set
        }
    },
    FUHYO("歩") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            val moved = place + player.forward
            if (!moved.isOnBoard) {
                return setOf()
            }
            val movedRectangle = board[moved]
            if (movedRectangle == null || movedRectangle.player != player) {
                return setOf(moved)
            }
            return setOf()
        }
    };

    companion object {
        // fun anyNumber of squares
    }

    abstract fun movements(place: Place, player: Player, board: BoardModel): Set<Place>

    override fun toString() = string
}
