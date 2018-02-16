package quantumshogi.components

import javafx.beans.binding.Bindings
import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.*
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import quantumshogi.pieces.Piece
import quantumshogi.pieces.QuantumPiece

class PieceCell: ListCell<QuantumPiece>() {
    private val hBox = HBox().apply {
        alignment = Pos.CENTER
    }
    private val stackPane = StackPane().apply {
        alignment = Pos.CENTER
    }
    private val label = Label().apply {
        isWrapText = true
        prefWidth = 160.0
    }

    init {
        text = null
        graphic = hBox
        prefHeight = 50.0

        hBox.children.add(stackPane)
        hBox.children.add(label)
    }

    override fun updateItem(item: QuantumPiece?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item != null && !empty) {
            stackPane.children.add(Rectangle(30.0, 40.0).apply {
                stroke = Color.BLACK
                strokeWidth = 1.0
                fill = Color.valueOf(item.player.color)
            })
            item.possibles.forEach {
                val text = Text(it.toString()).apply {
                    fill = Color.SKYBLUE
                }
                stackPane.children.add(text)
            }
            label.text = item.possibles.joinToString(",") { it.toString() }
        }
    }
}