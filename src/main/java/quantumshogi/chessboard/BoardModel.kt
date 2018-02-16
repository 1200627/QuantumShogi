package quantumshogi.chessboard

import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place
import quantumshogi.player.Player

data class BoardModel(
        private val pieces: Set<QuantumPiece> =
        setOf(0, 2).flatMap { rank -> (0..8).map { file -> QuantumPiece(Player.BLACK, Place(rank, file)) } }.toSet() +
                setOf(Place(1, 1), Place(1, 7)).map { QuantumPiece(Player.BLACK, it) }.toSet() +
                setOf(6, 8).flatMap { rank -> (0..8).map { file -> QuantumPiece(Player.WHITE, Place(rank, file)) } }.toSet() +
                setOf(Place(7, 1), Place(7, 7)).map { QuantumPiece(Player.WHITE, it) }.toSet(),
        val playing: Player = Player.BLACK) {

    operator fun get(place: Place) = pieces.singleOrNull { it.place == place }

    override fun toString() = (0..8).joinToString(separator = "") { rank ->
        (0..8).joinToString(separator = "", postfix = LINE_SEPARATOR) { file ->
            this[Place(rank, file)]?.player?.char ?: "□"
        }
    }

    fun movements(place: Place) = this[place]!!.possibles.flatMap {
        it.movements(place, playing, this)
    }

    fun moveToIfPossible(from: Place, to: Place): BoardModel {
        val fromPiece = this[from]
        if (fromPiece == null) {
            println("選択できてないよ")
            return this
        }
        if (fromPiece.player != playing) {
            println("自分の駒じゃないよ")
            return this
        }
        if (!fromPiece.possibles.any { it.movements(from, playing, this).contains(to) }) {
            println("そこには動かせないよ")
            return this
        }

        val filtered = fromPiece.possibles.filter { it.movements(from, playing, this).contains(to) }

        if (filtered.any { it.canPromote }) {
            if (to.rank in playing.promotableRank) {
                if (Chessboard.confirmPromote()) {
                    val newList = filtered.filter { it.canPromote }.map { it.promoted!! }.toList()
                    val toPiece = fromPiece.copy(place = to, possibles = newList.toMutableList())
                    val toExistPiece = this[to] ?: return BoardModel(pieces - fromPiece + toPiece, playing.nextPlayer)
                    return BoardModel(pieces - fromPiece - toExistPiece + toPiece, playing.nextPlayer)
                }
            }
        }

        val toPiece = fromPiece.copy(place = to, possibles = filtered.toMutableList())
        val toExistPiece = this[to] ?: return BoardModel(pieces - fromPiece + toPiece, playing.nextPlayer)
        return BoardModel(pieces - fromPiece - toExistPiece + toPiece, playing.nextPlayer)
    }


    companion object {
        val LINE_SEPARATOR = System.getProperty("line.separator")!!
    }
}