package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import quantumshogi.pieces.Piece
import quantumshogi.pieces.QuantumPiece

class Square(
        piece: Piece? = null,
        private val x: Int,
        private val y: Int
) : StackPane() {
    var piece: Piece? = null
        set(value) {
            field = value
            if (value != null) {
                val qp = value as QuantumPiece
                children.setAll(FXCollections.observableArrayList(qp))
                StackPane.setAlignment(qp, Pos.CENTER)
            }
        }

    init {
        this.piece = piece
        setOnMouseClicked {
            if (Chessboard.status != Chessboard.Status.SELECTED || !Chessboard.movable.contains(Pair(x, y))) {
                return@setOnMouseClicked
            }

            val selected = Chessboard.selected
            val selectedSquare = Chessboard.get(selected.first, selected.second)
            val selectedPiece = selectedSquare.piece
            selectedSquare.piece = null
            this.piece = selectedPiece

            Chessboard.clearStyle()
            Chessboard.playing = Chessboard.playing.nextPlayer
            Chessboard.status = Chessboard.Status.IDLE
        }
    }
}