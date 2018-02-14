package quantumshogi.pieces

enum internal class PieceType(val string: String) {
    FUHYO("歩兵"),
    HISHA("飛車"),
    KAKUGYO("角行"),
    KYOSHA("香車"),
    KEIMA("桂馬"),
    GIN("銀将"),
    KIN("金将"),
    OU("王将");

    override fun toString() = string
}