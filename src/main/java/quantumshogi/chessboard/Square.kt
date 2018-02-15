package quantumshogi.chessboard

import javafx.beans.property.SimpleBooleanProperty
import javafx.beans.property.SimpleObjectProperty
import quantumshogi.pieces.Piece
import quantumshogi.place.Place

class Square(
        piece: Piece?,
        var place: Place
) {
    val hasPieceProperty = SimpleBooleanProperty(false)
    val pieceProperty = SimpleObjectProperty(piece).apply {
        addListener { _, _, _ ->
            // これがないとfillPropertyのbindが効かない
        }
    }
    val enterableProperty = SimpleBooleanProperty(false)
    var piece: Piece? = null
        set(value) {
            field = value
            pieceProperty.value = value
            if (value == null) {
                hasPieceProperty.value = false
                return
            }
            hasPieceProperty.value = true
        }

    init {
        this.piece = piece
    }
}