package quantumshogi.chessboard

import javafx.collections.FXCollections
import javafx.collections.ObservableList
import quantumshogi.pieces.Piece
import quantumshogi.place.Place2
import quantumshogi.player.Turn

object BoardViewModel {
    private val squares by lazy { (0..80).map { Square() } }

    fun get(place: Place2.OnBoard): Square = squares[place.rank * 9 + place.file]

    fun clearEnterable() = squares.forEach { it.enterableProperty.value = false }

    fun showEnterable(places: Set<Place2.OnBoard>) = places.forEach { squares[it.rank * 9 + it.file].enterableProperty.value = true }

    fun updateView(boardModel: BoardModel) {
        (0..8).map { rank ->
            (0..8).map { file ->
                squares[rank * 9 + file].piece = boardModel[Place2.OnBoard(rank, file)]
            }
        }
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