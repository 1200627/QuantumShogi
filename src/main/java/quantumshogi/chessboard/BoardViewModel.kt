package quantumshogi.chessboard

import javafx.beans.property.SimpleStringProperty
import javafx.collections.FXCollections
import javafx.collections.ObservableList
import quantumshogi.pieces.Piece
import quantumshogi.places.InHand
import quantumshogi.places.OnBoard
import quantumshogi.player.Turn

object BoardViewModel {
    private val squares by lazy { (0..80).map { Square() } }

    val scoreSheet = SimpleStringProperty()

    fun updateScore(score: Score) {
        scoreSheet.value = score.toString()
    }

    fun get(place: OnBoard) = squares[place.toInt]

    fun clearEnterable() = squares.forEach { it.enterableProperty.value = false }

    fun showEnterable(places: Set<OnBoard>) = places.forEach { squares[it.toInt].enterableProperty.value = true }

    fun updateView(boardModel: BoardModel) = OnBoard.values.forEach {
        squares[it.toInt].piece = boardModel.pieceOnBoard(it)
    }


    private val blackHand by lazy { FXCollections.observableArrayList<Piece>() }
    private val whiteHand by lazy { FXCollections.observableArrayList<Piece>() }

    fun bindBlackHand(f: (ObservableList<Piece>) -> Unit) = f(blackHand)
    fun bindWhiteHand(f: (ObservableList<Piece>) -> Unit) = f(whiteHand)

    fun updateHands(boardModel: BoardModel) {
        blackHand.setAll(boardModel.getFromHand(InHand.of(Turn.BLACK)))
        whiteHand.setAll(boardModel.getFromHand(InHand.of(Turn.WHITE)))
    }
}