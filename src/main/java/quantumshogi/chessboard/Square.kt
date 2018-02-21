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
                pieceProperty.value = value
            }
        }
    val pieceProperty = SimpleObjectProperty(piece).apply { addListener { _, _, _ -> } }
    val enterableProperty = SimpleBooleanProperty(false)
}