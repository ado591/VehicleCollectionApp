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
}