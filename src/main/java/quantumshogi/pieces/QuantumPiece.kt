package quantumshogi.pieces

import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import quantumshogi.chessboard.Chessboard
import quantumshogi.chessboard.Place
import quantumshogi.player.Player

class QuantumPiece(
        val player: Player = Player.P1,
        var x: Int,
        var y: Int
) : Rectangle(30.0, 40.0, Color.valueOf(PIECE_COLOR)), Piece {
    private val possibles: List<PieceType> = PieceType.values().toMutableList()

    init {
        stroke = Color.BLACK
        strokeWidth = 1.0
        setOnMouseClicked {
            if (player != Chessboard.playing) {
                return@setOnMouseClicked
            }

            Chessboard.clearStyle()
            val possibleDestination = mutableListOf<Pair<Int, Int>>()
            if (possibles.contains(PieceType.FUHYO)) {
                possibleDestination.addAll(PieceType.FUHYO.movements(Place(y, x), player).map { it.file to it.rank })
             }
            //if (possibles.contains(PieceType.HISHA)) {
            //    possibleDestination.addAll(PieceType.HISHA.movements(Place(y, x), player).map { it.file to it.rank })
            //}
            ///if (possibles.contains(PieceType.KAKUGYO)) {
            //   possibleDestination.addAll(PieceType.KAKUGYO.movements(Place(y, x), player).map { it.file to it.rank })
            //}
            //if (possibles.contains(PieceType.KYOSHA)) {
            //    possibleDestination.addAll(PieceType.KYOSHA.movements(Place(y, x), player).map { it.file to it.rank })
            //}
            //if (possibles.contains(PieceType.KEIMA)) {
            //    possibleDestination.addAll(PieceType.KEIMA.movements(Place(y, x), player).map { it.file to it.rank })
            //}
            //if (possibles.contains(PieceType.GIN)) {
            //    possibleDestination.addAll(PieceType.GIN.movements(Place(y, x), player).map { it.file to it.rank })
            //}
            //if (possibles.contains(PieceType.KIN)) {
            //    possibleDestination.addAll(PieceType.KIN.movements(Place(y, x), player).map { it.file to it.rank })
            //}
            //if (possibles.contains(PieceType.OU)) {
            //    possibleDestination.addAll(PieceType.OU.movements(Place(y, x), player).map { it.file to it.rank })
            //}

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