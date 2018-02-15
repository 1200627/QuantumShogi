package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.geometry.Pos
import javafx.scene.layout.StackPane
import quantumshogi.pieces.Piece
import quantumshogi.pieces.QuantumPiece

class Square(
        piece: Piece?,
        var place: Place
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
            if (Chessboard.isNotMovableTo(place)) {
                return@setOnMouseClicked
            }
            Chessboard.moveTo(place)
        }
    }
}