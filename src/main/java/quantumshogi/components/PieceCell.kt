package quantumshogi.components

import javafx.geometry.Pos
import javafx.scene.control.Label
import javafx.scene.control.ListCell
import javafx.scene.layout.HBox
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Rectangle
import javafx.scene.text.Text
import quantumshogi.pieces.Piece

class PieceCell : ListCell<Piece>() {
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
        graphic = null
        prefHeight = 50.0

        hBox.children.add(stackPane)
        hBox.children.add(label)
    }

    override fun updateItem(item: Piece?, empty: Boolean) {
        super.updateItem(item, empty)
        if (item == null || empty) {
            graphic = null
            return
        }

        stackPane.children.add(Rectangle(30.0, 40.0).apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.valueOf(item.owner.color)
        })
        item.possibles.forEach {
            val text = Text(it.toString()).apply {
                fill = Color.SKYBLUE
            }
            stackPane.children.add(text)
        }
        label.text = item.possibles.joinToString(",") { it.toString() }

        graphic = hBox

    }
}