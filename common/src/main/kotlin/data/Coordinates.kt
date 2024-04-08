package data

const val MIN_X = -89
const val MAX_Y = 483f

data class Coordinates(
    var x: Int,
    var y: Float
) {
    constructor() : this(0, 0f)
}