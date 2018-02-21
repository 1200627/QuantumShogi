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

    fun updateItem(item: Piece) {
        stackPane.children.clear()
        stackPane.children.add(Polygon(
                0.0, 20.0,
                15.0, 10.0,
                15.0, -20.0,
                -15.0, -20.0,
                -15.0, 10.0,
                0.0, 20.0
        ).apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
            fill = Color.valueOf(item.initialOwner.color)
            rotate = if (item.owner == Turn.BLACK) {
                0.0
            } else {
                180.0
            }
            println(fill)
            println(rotate)
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