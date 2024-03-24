package data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonTypeName("vehicle")
data class Vehicle (
    @JsonIgnore
    var id: Long = genId(),
    val name: String,
    val coordinates: Coordinates,
    @JsonIgnore
    val creationDate: ZonedDateTime = ZonedDateTime.now(),
    val enginePower: Double,
    val fuelConsumption: Int,
    val type: VehicleType,
    val fuelType: FuelType
): Comparable<Vehicle> {

    companion object {
        private var currentId: Long = 0
        fun genId(): Long {
            currentId++
            return currentId
        }
    }

    override fun compareTo(other: Vehicle): Int {
        return compareValuesBy(this, other,
            { it.enginePower },
            { it.fuelConsumption },
            { it.creationDate },
            { it.name }
        )
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z")
        val stringRepresentation: StringBuilder  = StringBuilder()
            .appendLine("id: ${this.id}")
            .appendLine("name: ${this.name}")
            .appendLine("coordinates: x = ${this.coordinates.x}, " +
                    "y = ${this.coordinates.y}")
            .appendLine("creation date: ${this.creationDate.format(formatter)}")
            .appendLine("engine power: ${this.enginePower}")
            .appendLine("fuel consumption: ${this.fuelConsumption}")
            .appendLine("vehicle type: ${this.type}")
            .appendLine("fuel type: ${this.fuelType}")
        return stringRepresentation.toString()
    }
    constructor() : this(genId(), "unknown", Coordinates(), ZonedDateTime.now(), 0.01, 1, VehicleType.CHOPPER, FuelType.ANTIMATTER)
}


