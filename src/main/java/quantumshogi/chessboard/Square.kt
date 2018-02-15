package quantumshogi.chessboard

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import quantumshogi.pieces.QuantumPiece
import quantumshogi.place.Place

class Square(
        piece: QuantumPiece?,
        var place: Place
) {
    val hasPieceProperty = SimpleBooleanProperty(false)
    val pieceProperty = SimpleObjectProperty(piece).apply {
        addListener { _, _, _ -> }
    }
    val enterableProperty = SimpleBooleanProperty(false)
    var piece: QuantumPiece? = null
        set(value) {
            field = value
            Platform.runLater {
                hasPieceProperty.value = value != null
                pieceProperty.value = value
            }
        }

    init {
        this.piece = piece
    }
}