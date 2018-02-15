package quantumshogi.chessboard

import javafx.scene.control.Alert
import javafx.scene.control.ButtonType
import quantumshogi.pieces.PieceType
import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place
import quantumshogi.player.Player

object Chessboard {
    val boardView by lazy { (0..80).map { Square(null) } }

    private val boardModel by lazy { mutableSetOf<QuantumPiece>() }

    init {
        boardModel.clear()

        (0..8).map { rank ->
            (0..8).map { file ->
                val place by lazy { Place(rank, file) }
                when {
                    rank == 0 || rank == 2 -> boardModel.add(QuantumPiece(Player.BLACK, place))
                    rank == 1 && (file == 1 || file == 7) -> boardModel.add(QuantumPiece(Player.BLACK, place))
                    rank == 6 || rank == 8 -> boardModel.add(QuantumPiece(Player.WHITE, place))
                    rank == 7 && (file == 1 || file == 7) -> boardModel.add(QuantumPiece(Player.WHITE, place))
                    else -> {
                    }
                }
            }
        }

        updateView()
    }

    private fun updateView() {
        (0..8).map { rank ->
            (0..8).map { file ->
                val place by lazy { Place(rank, file) }
                boardView[rank * 9 + file].piece = boardModel.singleOrNull { it.place == place }
            }
        }
    }

    private var playing = Player.BLACK
    private var status = Status.IDLE
    private var selected: Place = Place(0, 0)
    private var movable: Set<Place> = emptySet()

    fun moveToIfPossible(to: Place): Boolean {
        if (status != Status.SELECTED || !movable.contains(to)) {
            return false
        }

        val before = toModel()

        val selectedPiece = boardModel.single { it.place == selected }
        if (!boardModel.removeIf { it.place == selected }) {
            throw IllegalStateException()
        }

        val movedPiece = QuantumPiece(selectedPiece.player, to)
        boardModel.add(movedPiece)

        clearEnterable()

        val cloned = movedPiece.possibles.toList()
        val filtered = cloned.filter { it.movements(selected, playing, before).contains(to) }
        println(PieceType.GOLD.movements(selected, playing, before))
        println("$cloned -> $filtered")

        movedPiece.possibles.clear()
        movedPiece.possibles.addAll(filtered)

        if (movedPiece.possibles.any { it.canPromote }) {
            if (to.rank in playing.promotableRank) {
                if (confirmPromote()) {
                    val newList = movedPiece.possibles.filter { it.canPromote }.map { it.promoted!! }.toList()
                    movedPiece.possibles.clear()
                    movedPiece.possibles.addAll(newList)
                    println(newList)
                }
            }
        }

        playing = playing.nextPlayer
        status = Status.IDLE
        println(toModel())

        updateView()
        return true
    }

    fun selectPiece(place: Place) {
        val player = toModel()[place]?.player
        if (playing != player) {
            return
        }

        clearEnterable()

        status = Status.SELECTED

        val possibleDestination = toModel()[place]?.possibles?.flatMap {
            it.movements(place, player, toModel())
        }?.toSet() ?: setOf()

        possibleDestination.forEach {
            val square = boardView[it.file + it.rank * 9]
            square.enterableProperty.value = true
        }

        movable = possibleDestination
        selected = place
        updateView()
    }

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
        boardView.forEach { it.enterableProperty.value = false }
    }

    operator fun get(place: Place): QuantumPiece? {
        return boardModel.singleOrNull { it.place == place }
    }

    private fun toModel(): BoardModel {
        return BoardModel(boardModel)
    }
}