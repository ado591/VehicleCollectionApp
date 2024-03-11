package data

import java.time.ZonedDateTime

data class Vehicle (
    private var id: Long,
    private var name: String,
    private var coordinates: Coordinates,
    private var creationDate: ZonedDateTime,
    private var enginePower: Double,
    private var fuelConsumption: Int,
    private var type: VehicleType,
    private var fuelType: FuelType
): Comparable<Vehicle> {

    override fun compareTo(other: Vehicle): Int {
        return compareValuesBy(this, other,
            { it.enginePower },
            { it.fuelConsumption },
            { it.creationDate },
            { it.name }
        )
    }

    fun getFuelConsumption(): Int {
        return fuelConsumption
    }

    override fun toString(): String {
        val stringRepresentation: StringBuilder  = StringBuilder()
            .append("id: ${this.id}\n")
            .append("name: ${this.name}\n")
            .append("coordinates: x = ${this.coordinates.getX()}, y = ${this.coordinates.getY()}\n")
            .append("creation date: ${this.creationDate}\n")
            .append("engine power: ${this.enginePower}\n")
            .append("fuel consumption: ${this.fuelConsumption}\n")
            .append("vehicle type: ${this.type}\n")
            .append("fuel type: ${this.fuelType}")
        return stringRepresentation.toString()
    }
}