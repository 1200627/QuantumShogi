package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import quantumshogi.pieces.Piece
import quantumshogi.pieces.QuantumPiece

class Square(
        piece: Piece?,
        val x: Int,
        val y: Int
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

            if (!Chessboard.pieceIsSelected() || !Chessboard.movable.contains(Pair(x, y))) {
                return@setOnMouseClicked
            }

            val (x, y) = Chessboard.selected
            val selectedSquare = Chessboard.get(x, y)
            val selectedPiece = selectedSquare.piece
            selectedSquare.piece = null
            this.piece = selectedPiece

            Chessboard.clearStyle()
            Chessboard.next()
            Chessboard.setIdol()

            println(Chessboard.toModel())
        }
    }

    override fun toString(): String {
        return "($x, $y) $piece"
    }
}