package quantumshogi.chessboard

import javafx.application.Platform
import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import quantumshogi.pieces.Piece

class Square {
    var piece: Piece? = null
        set(value) {
            field = value
            Platform.runLater {
                hasPieceProperty.value = value != null
                pieceProperty.value = value
            }
        }
    val hasPieceProperty = SimpleBooleanProperty(false)
    val pieceProperty = SimpleObjectProperty(piece).apply { addListener { _, _, _ -> } }
    val enterableProperty = SimpleBooleanProperty(false)
}