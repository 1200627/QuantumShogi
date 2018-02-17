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
        val all = (0..8).flatMap { rank -> (0..8).map { file -> Place2.OnBoard(rank, file) } }

        val notEmpties = pieces.map { it.place }

        return@lazy all.filter { !notEmpties.contains(it) }.toSet()
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
        if (filtered.any { it.promoted != null }) {
            if (to.rank in turn.promotableRank) {
                if (Chessboard.confirmPromote()) {
                    val newList = filtered.filter { it.promoted != null }.map { it.promoted!! }.toList()
                    val toPiece = fromPiece.copy(place = to, possibles = newList.toMutableList())
                    val toExistPiece = this[to]
                            ?: return copy(pieces = pieces - fromPiece + toPiece, turn = turn.next)

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
                    return copy(pieces = pieces - fromPiece - toExistPiece + toPiece + pieceAddedToHand, turn = turn.next)
                }
            }
        }

        val toPiece = fromPiece.copy(place = to, possibles = filtered.toMutableList())
        val toExistPiece = this[to]
                ?: return copy(pieces = pieces - fromPiece + toPiece, turn = turn.next)

        val pieceAddedToHand = toExistPiece.copy(
                owner = turn,
                place = Place2.InHand.of(turn),
                possibles = toExistPiece.possibles.map {
                    if (it.unpromoted == null) {
                        it
                    } else {
                        it.unpromoted!!
                    }
                })
        return copy(pieces = pieces - fromPiece - toExistPiece + toPiece + pieceAddedToHand, turn = turn.next)
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