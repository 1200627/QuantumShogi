package quantumshogi.pieces

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import quantumshogi.chessboard.Chessboard
import kotlin.math.abs

class QuantumPiece(
        val player: Piece.Player = Piece.Player.P1,
        var x: Int,
        var y: Int
) : Rectangle(30.0, 40.0, Color.valueOf(PIECE_COLOR)), Piece {
    private val possibles: List<Piece.Type> = Piece.Type.values().toMutableList()

    init {
        stroke = Color.BLACK
        strokeWidth = 1.0
        setOnMouseClicked {
            if (player != Chessboard.playing) {
                return@setOnMouseClicked
            }

            Chessboard.clearStyle()
            val possibleDestination = mutableListOf<Pair<Int, Int>>()
            if (possibles.contains(Piece.Type.FUHYO)) {
                val diff = y + player.direction
                if (diff in 0..8) {
                    possibleDestination.add(Pair(x, diff))
                }
            }
            if (possibles.contains(Piece.Type.HISHA)) {
                val list1 = (0 until 9).filter { it != x }.map {
                    Pair(it, y)
                }
                val list2 = (0 until 9).filter { it != y }.map {
                    Pair(x, it)
                }
                possibleDestination.addAll(list1 + list2)
            }
            if (possibles.contains(Piece.Type.KAKUGYO)) {
                val list = (0 until 9).filter { it != y }.flatMap {
                    val diff = abs(this.y - it)
                    listOf(Pair(x - diff, it), Pair(x + diff, it))
                }
                possibleDestination.addAll(list)
            }
            if (possibles.contains(Piece.Type.KYOSHA)) {
                val list = (1..(8 - y * player.direction) % 9).map {
                    Pair(x, y + it * player.direction)
                }
                possibleDestination.addAll(list)
            }
            if (possibles.contains(Piece.Type.KEIMA)) {
                val diffY = y + player.direction * 2
                possibleDestination.add(Pair(x - 1, diffY))
                possibleDestination.add(Pair(x + 1, diffY))
            }
            if (possibles.contains(Piece.Type.GIN)) {
                val diffY1 = y + player.direction
                val diffY2 = y - player.direction
                possibleDestination.add(Pair(x - 1, diffY1))
                possibleDestination.add(Pair(x, diffY1))
                possibleDestination.add(Pair(x + 1, diffY1))
                possibleDestination.add(Pair(x - 1, diffY2))
                possibleDestination.add(Pair(x + 1, diffY2))
            }
            if (possibles.contains(Piece.Type.KIN)) {
                val diffY1 = y + player.direction
                val diffY2 = y - player.direction
                possibleDestination.add(Pair(x - 1, diffY1))
                possibleDestination.add(Pair(x, diffY1))
                possibleDestination.add(Pair(x + 1, diffY1))
                possibleDestination.add(Pair(x - 1, y))
                possibleDestination.add(Pair(x + 1, y))
                possibleDestination.add(Pair(x, diffY2))
            }
            if (possibles.contains(Piece.Type.OU)) {
                val diffY1 = y + player.direction
                val diffY2 = y - player.direction
                possibleDestination.add(Pair(x - 1, diffY1))
                possibleDestination.add(Pair(x, diffY1))
                possibleDestination.add(Pair(x + 1, diffY1))
                possibleDestination.add(Pair(x - 1, y))
                possibleDestination.add(Pair(x + 1, y))
                possibleDestination.add(Pair(x - 1, diffY2))
                possibleDestination.add(Pair(x, diffY2))
                possibleDestination.add(Pair(x + 1, diffY2))
            }

            val checked = possibleDestination.distinct().filter {
                it.first in 0..8 && it.second in 0..8
            }.filter {
                val sq = Chessboard.get(it.first, it.second)
                val qp = (sq.piece as? QuantumPiece)
                qp == null || qp.player != Chessboard.playing
            }

            checked.forEach {
                val square = Chessboard.get(it.first, it.second)
                square.style = "-fx-background-color:#ff0000a0"
            }

            Chessboard.status = Chessboard.Status.SELECTED
            Chessboard.movable = checked
            Chessboard.selected = Pair(x, y)
        }
    }

    companion object {
        const val PIECE_COLOR = "#B55704"
    }
}