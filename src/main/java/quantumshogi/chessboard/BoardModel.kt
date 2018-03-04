package quantumshogi.chessboard

import quantumshogi.move.MoveOrDrop
import quantumshogi.pieces.Piece
import quantumshogi.pieces.PieceType
import quantumshogi.places.InHand
import quantumshogi.places.OnBoard
import quantumshogi.player.Turn

data class BoardModel(
        private val pieces: Set<Piece> = INITIAL_PIECES,
        private val turn: Turn = Turn.BLACK) {

    fun turnIs(turn: Turn) = this.turn == turn

    fun pieceOnBoard(place: OnBoard) = pieces.singleOrNull { it.place == place }

    fun getFromHand(place: InHand) = pieces.filter { it.place == place }

    override fun toString() = getFromHand(place = InHand.of(Turn.BLACK)).toString() +
            (0..8).joinToString(separator = LINE_SEPARATOR, prefix = LINE_SEPARATOR, postfix = LINE_SEPARATOR) { rank ->
                (0..8).joinToString(separator = "") { file ->
                    pieceOnBoard(OnBoard(rank, file))?.owner?.char?.toString() ?: "□"
                }
            } + getFromHand(place = InHand.of(Turn.WHITE)).toString()

    val emptiesOnBoard by lazy {
        val notEmpties = pieces.map { it.place }
        return@lazy OnBoard.values.filter { !notEmpties.contains(it) }.toSet()
    }

    fun movements(piece: Piece) = piece.possibles.flatMap {
        it.movements(piece, this)
    }

    fun moveToIfPossible(fromPiece: Piece, to: OnBoard): Pair<BoardModel, MoveOrDrop>? {
        if (fromPiece.owner != turn) {
            // println("自分の駒じゃないよ")
            return null
        }
        if (!fromPiece.possibles.any { it.movements(fromPiece, this).contains(to) }) {
            // println("そこには動かせないよ")
            return null
        }

        val filtered = fromPiece.possibles.filter { it.movements(fromPiece, this).contains(to) }

        // 成れるとき
        if (fromPiece.place.isOnBoard) {
            if (filtered.any { it.promoted != null }) {
                if (to.rank in turn.promotableRank) {
                    if (Chessboard.confirmPromote()) {
                        val newList = filtered.filter { it.promoted != null }.map { it.promoted!! }.toList()
                        val toPiece = fromPiece.copy(place = to, possibles = newList.toMutableList())
                        val toExistPiece = pieceOnBoard(to)
                                ?: return copy(pieces = updateMovementUsingInteraction(pieces - fromPiece + toPiece).toSet(), turn = turn.next) to MoveOrDrop(fromPiece, toPiece)

                        val pieceAddedToHand = toExistPiece.copy(
                                owner = turn,
                                place = InHand.of(turn),
                                possibles = toExistPiece.possibles.filter {
                                    it != PieceType.KING_HIGHER_RANKED_PLAYER &&
                                            it != PieceType.KING_LOWER_RANKED_PLAYER
                                }.map {
                                            if (it.unpromoted == null) {
                                                it
                                            } else {
                                                it.unpromoted!!
                                            }
                                        })
                        return copy(pieces = updateMovementUsingInteraction(pieces - fromPiece - toExistPiece + toPiece + pieceAddedToHand).toSet(), turn = turn.next) to MoveOrDrop(fromPiece, toPiece)
                    }
                }
            }
        }

        val toPiece = fromPiece.copy(place = to, possibles = filtered.toMutableList())
        val toExistPiece = pieceOnBoard(to)
                ?: return copy(pieces = updateMovementUsingInteraction(pieces - fromPiece + toPiece).toSet(), turn = turn.next) to MoveOrDrop(fromPiece, toPiece)


        val pieceAddedToHand = toExistPiece.copy(
                owner = turn,
                place = InHand.of(turn),
                possibles = toExistPiece.possibles.filter {
                    it != PieceType.KING_HIGHER_RANKED_PLAYER &&
                            it != PieceType.KING_LOWER_RANKED_PLAYER
                }.map {
                            if (it.unpromoted == null) {
                                it
                            } else {
                                it.unpromoted!!
                            }
                        })
        return copy(pieces = updateMovementUsingInteraction(pieces - fromPiece - toExistPiece + toPiece + pieceAddedToHand).toSet(), turn = turn.next) to MoveOrDrop(fromPiece, toPiece)
    }


    companion object {
        private val LINE_SEPARATOR by lazy { System.getProperty("line.separator")!! }
        private val INITIAL_PIECES by lazy {
            setOf(0, 2).flatMap { rank -> (0..8).map { file -> Piece(initialOwner = Turn.BLACK, place = OnBoard(rank, file)) } }.toSet() +
                    setOf(OnBoard(1, 1), OnBoard(1, 7)).map { Piece(initialOwner = Turn.BLACK, place = it) }.toSet() +
                    setOf(6, 8).flatMap { rank -> (0..8).map { file -> Piece(initialOwner = Turn.WHITE, place = OnBoard(rank, file)) } }.toSet() +
                    setOf(OnBoard(7, 1), OnBoard(7, 7)).map { Piece(initialOwner = Turn.WHITE, place = it) }.toSet()
        }


        // 入力：現在の盤面，更新対象のプレイヤー
        // 出力：他の駒の相互作用を考慮した盤面
        private fun updateMovementUsingInteraction(pieces: Set<Piece>): List<Piece> {
            val black = pieces
                    .filter { it.initialOwner == Turn.BLACK }
                    .sortedBy { it.possibles.size }
            val white = pieces
                    .filter { it.initialOwner == Turn.WHITE }
                    .sortedBy { it.possibles.size }
            return black.map { it.copy(possibles = filteringMovement(it, black - it)) } +
                    white.map { it.copy(possibles = filteringMovement(it, white - it)) }

        }

        // 入力：自分由来の駒のリストの一つ，残り
        // 出力：そのリスト中の駒の動きをフィルタリングしたリスト
        private fun filteringMovement(target: Piece, others: List<Piece>): List<PieceType> {
            return target.possibles.filter { existAnyFollowingTheRule(others, listOf(it)) }
        }

        private fun existAnyFollowingTheRule(others: List<Piece>, used: List<PieceType>): Boolean {
            if (others.isEmpty()) {
                return true
            }

            return others[0].possibles
                    .filter { isFollowedRule(used + it) }
                    .any { existAnyFollowingTheRule(others.drop(1), used + it) }
        }

        private fun isFollowedRule(used: List<PieceType>): Boolean {
            if (used.filter { it == PieceType.KING_HIGHER_RANKED_PLAYER || it == PieceType.KING_LOWER_RANKED_PLAYER }.size > 1) {
                return false
            }
            if (used.filter { it == PieceType.ROOK || it == PieceType.PROMOTED_ROOK }.size > 1) {
                return false
            }
            if (used.filter { it == PieceType.BISHOP || it == PieceType.PROMOTED_BISHOP }.size > 1) {
                return false
            }
            if (used.filter { it == PieceType.GOLD }.size > 2) {
                return false
            }
            if (used.filter { it == PieceType.SILVER || it == PieceType.PROMOTED_SILVER }.size > 2) {
                return false
            }
            if (used.filter { it == PieceType.KNIGHT || it == PieceType.PROMOTED_KNIGHT }.size > 2) {
                return false
            }
            if (used.filter { it == PieceType.LANCE || it == PieceType.PROMOTED_LANCE }.size > 2) {
                return false
            }
            if (used.filter { it == PieceType.PAWN || it == PieceType.PROMOTED_PAWN }.size > 9) {
                return false
            }
            return true
        }
    }


}
