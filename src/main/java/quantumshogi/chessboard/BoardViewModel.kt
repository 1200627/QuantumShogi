package quantumshogi.chessboard

import quantumshogi.place.Place

class BoardViewModel {
    private val squares by lazy { (0..80).map { Square() } }

    operator fun get(place: Place): Square = squares[place.rank * 9 + place.file]

    fun clearEnterable() = squares.forEach { it.enterableProperty.value = false }

    fun showEnterable(places: Set<Place>) = places.forEach { squares[it.rank * 9 + it.file].enterableProperty.value = true }

    fun updateView(boardModel: BoardModel) {
        (0..8).map { rank ->
            (0..8).map { file ->
                squares[rank * 9 + file].piece = boardModel[Place(rank, file)]
            }
        }
    }
}