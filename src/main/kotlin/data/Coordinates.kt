package data



data class Coordinates(
    private var x: Int,
    private var y: Float
) {
    fun getX(): Int {
        return this.x
    }

    fun getY(): Float {
        return this.y
    }
}