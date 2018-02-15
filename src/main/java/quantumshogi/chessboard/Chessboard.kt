package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import quantumshogi.pieces.QuantumPiece
import quantumshogi.player.Player

object Chessboard {
    private val rows: ObservableList<Square> = FXCollections.observableArrayList()

    private var playing = Player.P1
    private var status = Status.IDLE
    private var selected: Place = Place(0, 0)
    private var movable: Set<Place> = emptySet()

    fun isNotMovableTo(to: Place): Boolean {
        return !Chessboard.pieceIsSelected() || !Chessboard.movable.contains(to)
    }

    fun moveTo(to: Place) {
        val (y, x) = Chessboard.selected
        val selectedSquare = Chessboard.get(x, y)
        Chessboard.get(to.file, to.rank).piece = selectedSquare.piece
        selectedSquare.piece = null

        Chessboard.clearStyle()
        playing = playing.nextPlayer
        status = Status.IDLE

        rows.filter { it.piece != null }.forEach { it.piece!!.place = it.place }

        println(Chessboard.toModel())
    }

    fun selectPiece(place: Place, player: Player) {
        Chessboard.clearStyle()

        status = Chessboard.Status.SELECTED

        val possibleDestination = toModel()[place]?.possibles?.flatMap {
            it.movements(place, player, Chessboard.toModel())
        }?.toSet() ?: setOf()

        possibleDestination.forEach {
            val square = Chessboard.get(it.file, it.rank)
            square.style = "-fx-background-color:#ff0000a0"
        }

        movable = possibleDestination
        selected = place
    }

    fun pieceIsSelected(): Boolean {
        return status == Chessboard.Status.SELECTED
    }

    fun turnIs(player: Player): Boolean {
        return playing == player
    }

    init {
        (0..8).forEach { y ->
            (0..8).forEach { x ->
                val sq = when (y) {
                    0, 2 -> Square(QuantumPiece(Player.P1, Place(y, x)), Place(y, x))
                    1 -> {
                        when (x) {
                            1, 7 -> {
                                Square(QuantumPiece(Player.P1, Place(y, x)), Place(y, x))
                            }
                            else -> Square(null, Place(y, x))
                        }
                    }
                    6, 8 -> Square(QuantumPiece(Player.P2, Place(y, x)), Place(y, x))
                    7 -> {
                        when (x) {
                            1, 7 -> {
                                Square(QuantumPiece(Player.P2, Place(y, x)), Place(y, x))
                            }
                            else -> Square(null, Place(y, x))
                        }
                    }
                    else -> Square(null, Place(y, x))
                }
                rows.add(sq)
            }
        }
    }

    enum class Status {
        IDLE,
        SELECTED,
        MOVED
    }

    private fun clearStyle() {
        rows.forEach { it.style = "" }
    }

    fun get(x: Int, y: Int): Square = rows[x + y * 9]

    operator fun get(place: Place): Square {
        return rows[place.file + place.rank * 9]!!
    }

    private fun toModel(): BoardModel {
        return BoardModel(rows.filter { it.piece != null }.map { it.piece!! }.toSet())
    }
}