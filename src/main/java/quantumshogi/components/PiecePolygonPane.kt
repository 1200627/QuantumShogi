package quantumshogi.components

import javafx.beans.binding.Bindings
import javafx.beans.property.SimpleObjectProperty
import javafx.scene.layout.StackPane
import javafx.scene.paint.Color
import javafx.scene.shape.Polygon
import javafx.scene.text.Font
import javafx.scene.text.Text
import quantumshogi.pieces.Piece
import quantumshogi.player.Turn
import java.util.concurrent.Callable

class PiecePolygonPane : StackPane() {
    private val piecePolygon by lazy {
        Polygon(
                0.0, 20.0,
                15.0, 10.0,
                15.0, -20.0,
                -15.0, -20.0,
                -15.0, 10.0,
                0.0, 20.0).apply {
            stroke = Color.BLACK
            strokeWidth = 1.0
        }
    }
    val pieceProperty by lazy {
        SimpleObjectProperty<Piece>().apply {
            addListener { _, _, newValue ->
                this@PiecePolygonPane.children.removeAll { it is Text }
                if (newValue == null) {
                    return@addListener
                }
                this@PiecePolygonPane.children.addAll(newValue.possibles.map {
                    Text(it.alternateForm).apply {
                        fill = if (newValue.initialOwner == Turn.BLACK) {
                            Color.WHITE
                        } else {
                            Color.BLACK
                        }
                        font = Font.font("HGPGyoshotai", 24.0)
                    }
                })
            }
        }
    }

    init {
        piecePolygon.run {
            visibleProperty().bind(Bindings.createBooleanBinding(Callable {
                pieceProperty.value != null
            }, pieceProperty))
            disableProperty().bind(Bindings.createBooleanBinding(Callable {
                pieceProperty.value == null
            }, pieceProperty))
            fillProperty().bind(Bindings.createObjectBinding(Callable {
                Color.valueOf(pieceProperty.value?.initialOwner?.color ?: "#ffffff")
            }, pieceProperty))
            rotateProperty().bind(Bindings.createDoubleBinding(Callable {
                if (pieceProperty.value?.owner == Turn.BLACK) {
                    0.0
                } else {
                    180.0
                }
            }, pieceProperty))
        }

        this.children.add(piecePolygon)
    }
}