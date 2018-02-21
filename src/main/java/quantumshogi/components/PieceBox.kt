package quantumshogi.components

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.text.Text
import quantumshogi.pieces.Piece
import quantumshogi.player.Turn

class PieceBox : HBox() {
    private val stackPane = StackPane().apply {
        alignment = Pos.CENTER
    }
    private val label = Label().apply {
        isWrapText = true
        prefWidth = 160.0
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
            pieceProperty().value = item
        })
        label.text = item.possibles.joinToString(",") { it.toString() }
    }
}