package quantumshogi.pieces

import quantumshogi.chessboard.BoardModel
import quantumshogi.place.Place
import quantumshogi.player.Movement
import quantumshogi.player.Turn

enum class PieceType(private val string: String) {
    KING_HIGHER_RANKED_PLAYER("王将") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return setOf(
                    place + player.leftForward,
                    place + player.forward,
                    place + player.rightForward,
                    place + player.right,
                    place + player.left,
                    place + player.leftBackward,
                    place + player.backward,
                    place + player.rightBackward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    KING_LOWER_RANKED_PLAYER("玉将") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                KING_HIGHER_RANKED_PLAYER.movements(place, player, board)
    },
    ROOK("飛車") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { true }
        override val promoted by lazy { PROMOTED_ROOK }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return listOf(player.forward, player.left, player.backward, player.right).flatMap {
                anyNumberOfSquares(place, player, board, it)
            }.toSet()
        }
    },
    PROMOTED_ROOK("竜王") {
        override val isPromoted by lazy { true }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                ROOK.movements(place, player, board) + KING_HIGHER_RANKED_PLAYER.movements(place, player, board)

    },
    BISHOP("角行") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { true }
        override val promoted by lazy { PROMOTED_BISHOP }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return listOf(player.leftForward, player.leftBackward, player.rightForward, player.rightBackward).flatMap {
                anyNumberOfSquares(place, player, board, it)
            }.toSet()
        }
    },
    PROMOTED_BISHOP("竜馬") {
        override val isPromoted by lazy { true }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                BISHOP.movements(place, player, board) + KING_HIGHER_RANKED_PLAYER.movements(place, player, board)
    },
    GOLD("金将") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return setOf(
                    place + player.leftForward,
                    place + player.forward,
                    place + player.rightForward,
                    place + player.right,
                    place + player.left,
                    place + player.backward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    SILVER("銀将") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { true }
        override val promoted by lazy { PROMOTED_SILVER }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return setOf(
                    place + player.leftForward,
                    place + player.forward,
                    place + player.rightForward,
                    place + player.leftBackward,
                    place + player.rightBackward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_SILVER("成銀") {
        override val isPromoted by lazy { true }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                GOLD.movements(place, player, board)
    },
    KNIGHT("桂馬") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { true }
        override val promoted by lazy { PROMOTED_KNIGHT }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return setOf(
                    place + player.forward + player.rightForward,
                    place + player.forward + player.leftForward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_KNIGHT("成桂") {
        override val isPromoted by lazy { true }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                GOLD.movements(place, player, board)
    },
    LANCE("香車") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { true }
        override val promoted by lazy { PROMOTED_LANCE }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return anyNumberOfSquares(place, player, board, player.forward)
        }
    },
    PROMOTED_LANCE("成香") {
        override val isPromoted by lazy { true }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                GOLD.movements(place, player, board)
    },
    PAWN("歩兵") {
        override val isPromoted by lazy { false }
        override val canPromote by lazy { true }
        override val promoted by lazy { PROMOTED_PAWN }

        override fun movements(place: Place, player: Turn, board: BoardModel): Set<Place> {
            return setOf(place + player.forward).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_PAWN("と金") {
        override val isPromoted by lazy { true }
        override val canPromote by lazy { false }
        override val promoted by lazy { null }

        override fun movements(place: Place, player: Turn, board: BoardModel) =
                GOLD.movements(place, player, board)
    };

    companion object {
        fun anyNumberOfSquares(place: Place, player: Turn, board: BoardModel, direction: Movement): Set<Place> {
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

        fun isMine(place: Place, player: Turn, board: BoardModel): Boolean {
            val piece = board[place]
            return piece != null && piece.player == player
        }
    }

    abstract fun movements(place: Place, player: Turn, board: BoardModel): Set<Place>

    abstract val isPromoted: Boolean
    abstract val canPromote: Boolean
    abstract val promoted: PieceType?

    override fun toString() = string
}
