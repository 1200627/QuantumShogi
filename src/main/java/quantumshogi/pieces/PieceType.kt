package quantumshogi.pieces

import quantumshogi.chessboard.BoardModel
import quantumshogi.place.Place2
import quantumshogi.player.Movement
import quantumshogi.player.Turn

private operator fun Place2.OnBoard.plus(movement: Set<Movement>) =
        movement.map { this@plus + it }.toSet()

enum class PieceType(private val string: String, val alternateForm: String) {
    KING_HIGHER_RANKED_PLAYER("王将", "王") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard
            return (piece.place + player.oneSquareInAnyDirection)
                    .filter { it.isOnBoard }
                    .filter { !isMine(it, player, board) }
                    .toSet()
        }
    },
    KING_LOWER_RANKED_PLAYER("玉将", "玉") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                KING_HIGHER_RANKED_PLAYER.movements(piece, player, board)
    },
    ROOK("飛車", "飛") {
        override val promoted by lazy { PROMOTED_ROOK }
        override val unpromoted: Nothing?  by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            if (!piece.place.isOnBoard) {
                return board.emptiesOnBoard
            }
            piece.place as Place2.OnBoard
            return player.oneSquareOrthogonally.flatMap {
                anyNumberOfSquares(piece.place, player, board, it)
            }.toSet()
        }
    },
    PROMOTED_ROOK("竜王", "竜") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted by lazy { ROOK }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard
            return ROOK.movements(piece, player, board) + KING_HIGHER_RANKED_PLAYER.movements(piece, player, board)
        }
    },
    BISHOP("角行", "角") {
        override val promoted by lazy { PROMOTED_BISHOP }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard
            return player.oneSquareDiagonally.flatMap {
                anyNumberOfSquares(piece.place, player, board, it)
            }.toSet()
        }
    },
    PROMOTED_BISHOP("竜馬", "馬") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted by lazy { BISHOP }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                BISHOP.movements(piece, player, board) + KING_HIGHER_RANKED_PLAYER.movements(piece, player, board)
    },
    GOLD("金将", "金") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard
            piece.place + (player.oneSquareOrthogonally + player.oneSquareStraightForward)

            return (piece.place + (player.oneSquareOrthogonally + player.oneSquareDiagonallyForward))
                    .filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    SILVER("銀将", "銀") {
        override val promoted by lazy { PROMOTED_SILVER }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard

            return (piece.place + (player.oneSquareDiagonally + player.oneSquareStraightForward))
                    .filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_SILVER("成銀", "全") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted by lazy { SILVER }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    },
    KNIGHT("桂馬", "桂") {
        override val promoted by lazy { PROMOTED_KNIGHT }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard
                    .filter { (it + player.oneSquareStraightForward + player.oneSquareStraightForward).isOnBoard }.toSet()

            return ((piece.place + player.oneSquareDiagonallyForward.map { it + player.oneSquareStraightForward }.toSet()))
                    .filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_KNIGHT("成桂", "圭") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted by lazy { KNIGHT }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    },
    LANCE("香車", "香") {
        override val promoted by lazy { PROMOTED_LANCE }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard ?: return board.emptiesOnBoard
                    .filter { (it + player.oneSquareStraightForward).isOnBoard }.toSet()
            return anyNumberOfSquares(piece.place, player, board, player.oneSquareStraightForward)
        }
    },
    PROMOTED_LANCE("成香", "杏") {
        override val promoted: Nothing? by lazy { null }
        override val unpromoted by lazy { LANCE }

        override fun movements(piece: Piece, player: Turn, board: BoardModel) =
                GOLD.movements(piece, player, board)
    },
    PAWN("歩兵", "歩") {
        override val promoted by lazy { PROMOTED_PAWN }
        override val unpromoted: Nothing? by lazy { null }

        override fun movements(piece: Piece, player: Turn, board: BoardModel): Set<Place2.OnBoard> {
            piece.place as? Place2.OnBoard
                    ?: return board.emptiesOnBoard.filter { (it + player.oneSquareStraightForward).isOnBoard }.toSet()
            return setOf(piece.place + player.oneSquareStraightForward)
                    .filter { it.isOnBoard }.filter { !isMine(it, player, board) }.toSet()
        }
    },
    PROMOTED_PAWN("と金", "と") {
        override val promoted: Nothing? by lazy { null }
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
