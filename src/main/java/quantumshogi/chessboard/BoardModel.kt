package quantumshogi.chessboard

import quantumshogi.pieces.Piece
import quantumshogi.pieces.PieceType
import quantumshogi.place.Place2
import quantumshogi.player.Turn

data class BoardModel(
        private val pieces: Set<Piece> = INITIAL_PIECES,
        private val turn: Turn = Turn.BLACK) {

    fun turnIs(turn: Turn) = this.turn == turn

    operator fun get(place: Place2.OnBoard) = pieces.singleOrNull { it.place == place }

    fun getFromHand(place: Place2.InHand) = pieces.filter { it.place == place }

    override fun toString() = getFromHand(place = Place2.InHand.of(Turn.BLACK)).toString() +
            (0..8).joinToString(separator = LINE_SEPARATOR, prefix = LINE_SEPARATOR, postfix = LINE_SEPARATOR) { rank ->
                (0..8).joinToString(separator = "") { file ->
                    this[Place2.OnBoard(rank, file)]?.owner?.char ?: "□"
                }
            } + getFromHand(place = Place2.InHand.of(Turn.WHITE)).toString()

    val emptiesOnBoard by lazy {
        val notEmpties = pieces.map { it.place }
        return@lazy Place2.OnBoard.values.filter { !notEmpties.contains(it) }.toSet()
    }

    fun movements(piece: Piece) = piece.possibles.flatMap {
        it.movements(piece, turn, this)
    }

    fun moveToIfPossible(fromPiece: Piece, to: Place2.OnBoard): BoardModel {
        if (fromPiece.owner != turn) {
            println("自分の駒じゃないよ")
            return this
        }
        if (!fromPiece.possibles.any { it.movements(fromPiece, turn, this).contains(to) }) {
            println("そこには動かせないよ")
            return this
        }

        val filtered = fromPiece.possibles.filter { it.movements(fromPiece, turn, this).contains(to) }

        // 成れるとき
        if (fromPiece.place.isOnBoard) {
            if (filtered.any { it.promoted != null }) {
                if (to.rank in turn.promotableRank) {
                    if (Chessboard.confirmPromote()) {
                        val newList = filtered.filter { it.promoted != null }.map { it.promoted!! }.toList()
                        val toPiece = fromPiece.copy(place = to, possibles = newList.toMutableList())
                        val toExistPiece = this[to]
                                ?: return copy(pieces = aaa(pieces - fromPiece + toPiece, turn).toSet(), turn = turn.next)

                        val pieceAddedToHand = toExistPiece.copy(
                                owner = turn,
                                place = Place2.InHand.of(turn),
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
                        return copy(pieces = aaa(pieces - fromPiece - toExistPiece + toPiece + pieceAddedToHand, turn).toSet(), turn = turn.next)
                    }
                }
            }
        }

        val toPiece = fromPiece.copy(place = to, possibles = filtered.toMutableList())
        val toExistPiece = this[to]
                ?: return copy(pieces = aaa(pieces - fromPiece + toPiece, turn).toSet(), turn = turn.next)



        val pieceAddedToHand = toExistPiece.copy(
                owner = turn,
                place = Place2.InHand.of(turn),
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
        return copy(pieces = aaa(pieces - fromPiece - toExistPiece + toPiece + pieceAddedToHand, turn).toSet(), turn = turn.next)
    }


    companion object {
        val LINE_SEPARATOR by lazy { System.getProperty("line.separator")!! }
        val INITIAL_PIECES by lazy {
            setOf(0, 2).flatMap { rank -> (0..8).map { file -> Piece(initialOwner = Turn.BLACK, place = Place2.OnBoard(rank, file)) } }.toSet() +
                    setOf(Place2.OnBoard(1, 1), Place2.OnBoard(1, 7)).map { Piece(initialOwner = Turn.BLACK, place = it) }.toSet() +
                    setOf(6, 8).flatMap { rank -> (0..8).map { file -> Piece(initialOwner = Turn.WHITE, place = Place2.OnBoard(rank, file)) } }.toSet() +
                    setOf(Place2.OnBoard(7, 1), Place2.OnBoard(7, 7)).map { Piece(initialOwner = Turn.WHITE, place = it) }.toSet()
        }
    }


}

// 入力：現在の盤面，更新対象のプレイヤー
// 出力：他の駒の相互作用を考慮した盤面
fun aaa(pieces: Set<Piece>, turn: Turn): List<Piece> {
    val black = pieces
            .filter { it.initialOwner == Turn.BLACK }
            .sortedBy { it.possibles.size }
    println("black = $black")

    val white = pieces
            .filter { it.initialOwner == Turn.WHITE }
            .sortedBy { it.possibles.size }
    if (turn == Turn.BLACK) {
        return white + black.map { it.copy(possibles = filteringMovement(it, black - it)) }
    }
    return black + white.map { it.copy(possibles = filteringMovement(it, white - it)) }

}

// 入力：自分由来の駒のリストの一つ，残り
// 出力：そのリスト中の駒の動きをフィルタリングしたリスト
fun filteringMovement(target: Piece, others: List<Piece>): List<PieceType> {
    return target.possibles.filter { existAnyFollowingTheRule(others, listOf(it)) }
}

fun existAnyFollowingTheRule(others: List<Piece>, used: List<PieceType>): Boolean {
    if (others.isEmpty()) {
        return true
    }

    return others[0].possibles
            .filter { isFollowedRule(used + it) }
            .any { existAnyFollowingTheRule(others.drop(1), used + it) }
}

fun isFollowedRule(used: List<PieceType>): Boolean {
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