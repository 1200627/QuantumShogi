package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import quantumshogi.pieces.QuantumPiece
import quantumshogi.player.Player

object Chessboard {
    private val rows: ObservableList<Square> = FXCollections.observableArrayList()

    private var playing = Player.P1
    private var status = Status.IDLE
    var selected: Pair<Int, Int> = Pair(0, 0)
    var movable: List<Pair<Int, Int>> = emptyList()

    fun update(set: Set<Square>): Boolean {
        return rows.setAll(set)
    }

    fun selectPiece() {
        status = Chessboard.Status.SELECTED
    }

    fun pieceIsSelected(): Boolean {
        return status == Chessboard.Status.SELECTED
    }

    fun setIdol() {
        status = Status.IDLE
    }

    fun turnIs(player: Player): Boolean {
        return playing == player
    }

    fun next() {
        playing = playing.nextPlayer
    }

    init {
        initialize()
    }

    enum class Status {
        IDLE,
        SELECTED,
        MOVED
    }

    fun clearStyle() {
        rows.forEach { it.style = "" }
    }

    fun initialize() {
        (0 until 9).forEach { y ->
            (0 until 9).forEach { x ->
                val sq = when (y) {
                    0, 2 -> Square(QuantumPiece(Player.P1, x, y), x, y)
                    1 -> {
                        when (x) {
                            1, 7 -> {
                                Square(QuantumPiece(Player.P1, x, y), x, y)
                            }
                            else -> Square(null, x, y)
                        }
                    }
                    6, 8 -> Square(QuantumPiece(Player.P2, x, y), x, y)
                    7 -> {
                        when (x) {
                            1, 7 -> {
                                Square(QuantumPiece(Player.P2, x, y), x, y)
                            }
                            else -> Square(null, x, y)
                        }
                    }
                    else -> Square(null, x, y)
                }
                rows.add(sq)
            }
        }
    }

    fun get(x: Int, y: Int): Square = rows[x + y * 9]

    fun toModel(): BoardModel {
        return BoardModel(rows.filter { it.piece != null }.associate {
            Place(it.y, it.x) to it.piece!!
        })
    }
}