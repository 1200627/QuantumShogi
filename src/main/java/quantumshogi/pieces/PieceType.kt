package quantumshogi.pieces

import quantumshogi.chessboard.BoardModel
import quantumshogi.chessboard.Place
import quantumshogi.player.Movement
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
            ).filter { !isMine(it, player, board) }.toSet()
        }
    },
    ROOK("飛") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return listOf(player.forward, player.left, player.backward, player.right).flatMap {
                anyNumberOfSquares(place, player, board, it)
            }.toSet()
        }
    },
    BISHOP("角") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return listOf(player.leftForward, player.leftBackward, player.rightForward, player.rightBackward).flatMap {
                anyNumberOfSquares(place, player, board, it)
            }.toSet()
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
            ).filter { !isMine(it, player, board) }.toSet()
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
            ).filter { !isMine(it, player, board) }.toSet()
        }
    },
    KEIMA("桂") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return setOf(
                    Place(place.rank + 2 * player.direction, place.file + 1),
                    Place(place.rank + 2 * player.direction, place.file - 1)
            ).filter { !isMine(it, player, board) }.toSet()
        }
    },
    KYOSHA("香") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return anyNumberOfSquares(place, player, board, player.forward)
        }
    },
    FUHYO("歩") {
        override fun movements(place: Place, player: Player, board: BoardModel): Set<Place> {
            return setOf(
                    Place(place.rank + 1 * player.direction, place.file)
            ).filter { !isMine(it, player, board) }.toSet()
        }
    };

    companion object {
        fun anyNumberOfSquares(place: Place, player: Player, board: BoardModel, direction: Movement): Set<Place> {
            val set = mutableSetOf<Place>()

            var now = place
            while (true) {
                now += direction
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

        fun isMine(place: Place, player: Player, board: BoardModel): Boolean {
            val piece = board[place]
            return piece != null && piece.player == player
        }
    }

    abstract fun movements(place: Place, player: Player, board: BoardModel): Set<Place>

    override fun toString() = string
}
