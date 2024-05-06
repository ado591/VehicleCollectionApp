package data

enum class VehicleType(val id: Int) {
    SHIP(1),
    CHOPPER(2),
    HOVERBOARD(3)
}

fun getTypeById(id: Int): VehicleType? {
    return VehicleType.entries.find { it.id == id }
}