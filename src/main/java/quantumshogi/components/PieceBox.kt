package quantumshogi.components

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import quantumshogi.pieces.Piece

class PieceBox : HBox() {
    private val stackPane by lazy {
        StackPane().apply {
            alignment = Pos.CENTER
        }
    }
    private val label by lazy {
        Label().apply {
            isWrapText = true
            prefWidth = 160.0
        }
    }

    init {
        alignment = Pos.CENTER
        children.addAll(stackPane, label)
    }

    fun updateItem(item: Piece?) {
        stackPane.children.clear()
        if (item == null) {
            label.text = ""
            return
        }

        stackPane.children.add(PiecePolygonPane().apply {
            pieceProperty.value = item
        })
        label.text = item.possibles.joinToString(",") { it.toString() }
    }
}