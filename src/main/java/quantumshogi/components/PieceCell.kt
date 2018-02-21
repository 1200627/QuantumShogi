package quantumshogi.components

import javafx.scene.control.ListCell
import quantumshogi.pieces.Piece

class PieceCell : ListCell<Piece>() {
    private val pieceBox by lazy { PieceBox() }

    init {
        text = null
        graphic = null
        prefHeight = 50.0
    }

    override fun updateItem(item: Piece?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item == null || empty) {
            graphic = null
            return
        }
        pieceBox.updateItem(item)

        graphic = pieceBox
    }
}