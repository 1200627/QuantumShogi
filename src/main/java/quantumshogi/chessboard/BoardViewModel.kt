package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import quantumshogi.pieces.Piece
import quantumshogi.place.Place2
import quantumshogi.player.Turn

object BoardViewModel {
    private val squares by lazy { (0..80).map { Square() } }

    fun get(place: Place2.OnBoard): Square = squares[place.toInt]

    fun clearEnterable() = squares.forEach { it.enterableProperty.value = false }

    fun showEnterable(places: Set<Place2.OnBoard>) = places.forEach { squares[it.toInt].enterableProperty.value = true }

    fun updateView(boardModel: BoardModel) = Place2.OnBoard.values.forEach {
        squares[it.toInt].piece = boardModel[it]
    }


    private val blackHand by lazy { FXCollections.observableArrayList<Piece>() }
    private val whiteHand by lazy { FXCollections.observableArrayList<Piece>() }

    fun bindBlackHand(f: (ObservableList<Piece>) -> Unit) = f(blackHand)
    fun bindWhiteHand(f: (ObservableList<Piece>) -> Unit) = f(whiteHand)

    fun updateHands(boardModel: BoardModel) {
        blackHand.setAll(boardModel.getFromHand(Place2.InHand.of(Turn.BLACK)))
        whiteHand.setAll(boardModel.getFromHand(Place2.InHand.of(Turn.WHITE)))
    }
}