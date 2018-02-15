package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place
import quantumshogi.player.Player

object Chessboard {
    private val rows = mutableListOf<Square>()

    private var playing = Player.BLACK
    private var status = Status.IDLE
    private var selected: Place = Place(0, 0)
    private var movable: Set<Place> = emptySet()


    fun moveToIfPossible(to: Place): Boolean {
        if (status != Chessboard.Status.SELECTED || !Chessboard.movable.contains(to)) {
            return false
        }

        val (y, x) = Chessboard.selected
        val selectedSquare = Chessboard.get(x, y)
        Chessboard.get(to.file, to.rank).piece = selectedSquare.piece
        selectedSquare.piece = null

        Chessboard.clearEnterable()
        playing = playing.nextPlayer
        status = Status.IDLE

        rows.filter { it.piece != null }.forEach { it.piece!!.place = it.place }

        println(Chessboard.toModel())
        return true
    }

    fun selectPiece(place: Place) {
        val player = toModel()[place]?.player
        if (!turnIs(player ?: return)) {
            return
        }

        Chessboard.clearEnterable()

        status = Chessboard.Status.SELECTED

        val possibleDestination = toModel()[place]?.possibles?.flatMap {
            it.movements(place, player, Chessboard.toModel())
        }?.toSet() ?: setOf()

        possibleDestination.forEach {
            val square = Chessboard.get(it.file, it.rank)
            square.enterableProperty.value = true
        }

        movable = possibleDestination
        selected = place
    }

    fun turnIs(player: Player): Boolean {
        return playing == player
    }

    /**
     * 成るかどうかを確認するダイアログを表示するメソッド．
     * YESであればtrueを返す．
     */
    fun confirmPromote(): Boolean {
        val alert = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
            title = "確認"
        }
        val selected = alert.showAndWait().orElse(ButtonType.NO)
        return selected == ButtonType.YES
    }

    init {
        (0..8).forEach { y ->
            (0..8).forEach { x ->
                val sq = when (y) {
                    0, 2 -> Square(QuantumPiece(Player.BLACK, Place(y, x)), Place(y, x))
                    1 -> {
                        when (x) {
                            1, 7 -> {
                                Square(QuantumPiece(Player.BLACK, Place(y, x)), Place(y, x))
                            }
                            else -> Square(null, Place(y, x))
                        }
                    }
                    6, 8 -> Square(QuantumPiece(Player.WHITE, Place(y, x)), Place(y, x))
                    7 -> {
                        when (x) {
                            1, 7 -> {
                                Square(QuantumPiece(Player.WHITE, Place(y, x)), Place(y, x))
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

    private fun clearEnterable() {
        rows.forEach { it.enterableProperty.value = false }
    }

    fun get(x: Int, y: Int): Square = rows[x + y * 9]

    operator fun get(place: Place): Square {
        return rows[place.file + place.rank * 9]
    }

    private fun toModel(): BoardModel {
        return BoardModel(rows.filter { it.piece != null }.map { it.piece!! }.toSet())
    }
}