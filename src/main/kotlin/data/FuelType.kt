package data

enum class FuelType(val id: Int) {
    GASOLINE(1),
    DIESEL(2),
    MANPOWER(3),
    ANTIMATTER(4)
}
fun getFuelTypeById(id: Int): FuelType? {
    return FuelType.entries.find { it.id == id }
}