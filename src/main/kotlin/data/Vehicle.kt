package data

import java.time.ZonedDateTime

data class Vehicle (
    var id: Long,
    var name: String,
    var coordinates: Coordinates,
    var creationDate: ZonedDateTime,
    var enginePower: Double,
    var fuelConsumption: Int,
    var type: VehicleType,
    var fuelType: FuelType
): Comparable<Vehicle> {

    override fun compareTo(other: Vehicle): Int {
        return compareValuesBy(this, other,
            { it.enginePower },
            { it.fuelConsumption },
            { it.creationDate },
            { it.name }
        )
    }

    override fun toString(): String {
        val stringRepresentation: StringBuilder  = StringBuilder()
            .append("id: ${this.id}\n")
            .append("name: ${this.name}\n")
            .append("coordinates: x = ${this.coordinates.x}, y = ${this.coordinates.y}\n")
            .append("creation date: ${this.creationDate}\n")
            .append("engine power: ${this.enginePower}\n")
            .append("fuel consumption: ${this.fuelConsumption}\n")
            .append("vehicle type: ${this.type}\n")
            .append("fuel type: ${this.fuelType}")
        return stringRepresentation.toString()
    }
}