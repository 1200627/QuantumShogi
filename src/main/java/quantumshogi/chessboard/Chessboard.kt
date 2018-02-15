package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.PieceType
import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place
import quantumshogi.player.Player

object Chessboard {
    private val rows by lazy { (0..8).flatMap { rank -> (0..8).map { file -> Square(null, Place(rank, file)) } } }

    init {
        (0..8).map { rank ->
            (0..8).map { file ->
                val place by lazy { Place(rank, file) }
                rows[rank * 9 + file].piece = when (rank) {
                    0, 2 -> QuantumPiece(Player.BLACK, place)
                    1 -> {
                        when (file) {
                            1, 7 -> {
                                QuantumPiece(Player.BLACK, place)
                            }
                            else -> null
                        }
                    }
                    6, 8 -> QuantumPiece(Player.WHITE, place)
                    7 -> {
                        when (file) {
                            1, 7 -> {
                                QuantumPiece(Player.WHITE, place)
                            }
                            else -> null
                        }
                    }
                    else -> null
                }
            }
        }
    }

    private var playing = Player.BLACK
    private var status = Status.IDLE
    private var selected: Place = Place(0, 0)
    private var movable: Set<Place> = emptySet()


    fun moveToIfPossible(to: Place): Boolean {
        if (status != Chessboard.Status.SELECTED || !Chessboard.movable.contains(to)) {
            return false
        }

        val before = toModel()

        val (y, x) = Chessboard.selected
        val selectedSquare = Chessboard.get(x, y)
        Chessboard.get(to.file, to.rank).piece = selectedSquare.piece
        selectedSquare.piece = null

        Chessboard.clearEnterable()

        //rows[x + y * 9].piece = null
        //rows[to.file + to.rank * 9].piece = QuantumPiece(playing, selected)
        rows.filter { it.piece != null }.forEach { it.piece!!.place = it.place }

        val cloned = Chessboard.get(to.file, to.rank).piece!!.possibles.toList()
        val filtered = cloned.filter { it.movements(selected, playing, before).contains(to) }
        println(PieceType.GOLD.movements(selected, playing, before))
        println("$cloned -> $filtered")

        Chessboard.get(to.file, to.rank).piece!!.possibles.clear()
        Chessboard.get(to.file, to.rank).piece!!.possibles.addAll(filtered)

        if (Chessboard.get(to.file, to.rank).piece!!.possibles.any { it.canPromote }) {
            if (to.rank in playing.promotableRank) {
                if (confirmPromote()) {
                    val newList = Chessboard.get(to.file, to.rank).piece!!.possibles.filter { it.canPromote }.map { it.promoted!! }.toList()
                    Chessboard.get(to.file, to.rank).piece!!.possibles.clear()
                    Chessboard.get(to.file, to.rank).piece!!.possibles.addAll(newList)
                    println(newList)
                }
            }
        }

        playing = playing.nextPlayer
        status = Status.IDLE
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
    private fun confirmPromote(): Boolean {
        val alert = Alert(Alert.AlertType.NONE, "成りますか？", ButtonType.YES, ButtonType.NO).apply {
            title = "確認"
        }
        val selected = alert.showAndWait().orElse(ButtonType.NO)
        return selected == ButtonType.YES
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

    operator fun set(place: Place, element: QuantumPiece): QuantumPiece? {
        val old = rows[place.rank * 9 + place.file].piece
        rows[place.rank * 9 + place.file].piece = element
        return old
    }

    operator fun get(place: Place): Square {
        return rows[place.file + place.rank * 9]
    }

    private fun toModel(): BoardModel {
        return BoardModel(rows.filter { it.piece != null }.map { it.piece!! }.toSet())
    }
}