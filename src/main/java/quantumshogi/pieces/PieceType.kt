package quantumshogi.pieces

import quantumshogi.chessboard.BoardModel
import quantumshogi.place.Place2
import quantumshogi.player.Movement
import quantumshogi.player.Turn

enum class PieceType(private val string: String) {
    KING_HIGHER_RANKED_PLAYER("王将") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return setOf(
                    piece.place + player.leftForward,
                    piece.place + player.forward,
                    piece.place + player.rightForward,
                    piece.place + player.right,
                    piece.place + player.left,
                    piece.place + player.leftBackward,
                    piece.place + player.backward,
                    piece.place + player.rightBackward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    KING_LOWER_RANKED_PLAYER("玉将") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                KING_HIGHER_RANKED_PLAYER.movements(piece, player, board)
    },
    ROOK("飛車") {
        override val promoted by lazy { PROMOTED_ROOK }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return listOf(player.forward, player.left, player.backward, player.right).flatMap {
                anyNumberOfSquares(piece.place, player, board, it)
            }.toSet()
        }
    },
    PROMOTED_ROOK("竜王") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { ROOK }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            return ROOK.movements(piece, player, board) + KING_HIGHER_RANKED_PLAYER.movements(piece, player, board)
        }
    },
    BISHOP("角行") {
        override val promoted by lazy { PROMOTED_BISHOP }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return listOf(player.leftForward, player.leftBackward, player.rightForward, player.rightBackward).flatMap {
                anyNumberOfSquares(piece.place, player, board, it)
            }.toSet()
        }
    },
    PROMOTED_BISHOP("竜馬") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { BISHOP }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                BISHOP.movements(piece, player, board) + KING_HIGHER_RANKED_PLAYER.movements(piece, player, board)
    },
    GOLD("金将") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return setOf(
                    piece.place + player.leftForward,
                    piece.place + player.forward,
                    piece.place + player.rightForward,
                    piece.place + player.right,
                    piece.place + player.left,
                    piece.place + player.backward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    SILVER("銀将") {
        override val promoted by lazy { PROMOTED_SILVER }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return setOf(
                    piece.place + player.leftForward,
                    piece.place + player.forward,
                    piece.place + player.rightForward,
                    piece.place + player.leftBackward,
                    piece.place + player.rightBackward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_SILVER("成銀") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { SILVER }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    },
    KNIGHT("桂馬") {
        override val promoted by lazy { PROMOTED_KNIGHT }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return setOf(
                    piece.place + player.forward + player.rightForward,
                    piece.place + player.forward + player.leftForward
            ).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_KNIGHT("成桂") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { KNIGHT }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    },
    LANCE("香車") {
        override val promoted by lazy { PROMOTED_LANCE }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return anyNumberOfSquares(piece.place, player, board, player.forward)
        }
    },
    PROMOTED_LANCE("成香") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { LANCE }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    },
    PAWN("歩兵") {
        override val promoted by lazy { PROMOTED_PAWN }
        override val unpromoted by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return setOf(piece.place + player.forward).filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_PAWN("と金") {
        override val promoted by lazy { null }
        override val unpromoted by lazy { PAWN }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    };

    companion object {
        fun anyNumberOfSquares(place: Place2.OnBoard, player: Turn, board: BoardModel, direction: Movement): Set<Place2.OnBoard> {
            val set = mutableSetOf<Place2.OnBoard>()

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
                if (movedRectangle.owner != player) {
                    set.add(now)
                    break
                }
                break
            }

            return set
        }

        fun isMine(place: Place2.OnBoard, player: Turn, board: BoardModel): Boolean {
            val piece = board[place]
            return piece != null && piece.owner == player
        }
    }

    abstract fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard>


    abstract val promoted: PieceType?
    abstract val unpromoted: PieceType?

    override fun toString() = string
}
