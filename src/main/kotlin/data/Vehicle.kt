package data

import com.fasterxml.jackson.annotation.JsonIgnore
import com.fasterxml.jackson.annotation.JsonTypeName
import commands.extra.Validatable
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonTypeName("vehicle")
data class Vehicle (
    @JsonIgnore
    var id: Long = genId(),
    var name: String,
    var coordinates: Coordinates,
    @JsonIgnore
    var creationDate: ZonedDateTime = ZonedDateTime.now(),
    var enginePower: Double,
    var fuelConsumption: Int,
    var type: VehicleType,
    var fuelType: FuelType
): Comparable<Vehicle>, Validatable {




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

    override fun isValid(): Boolean {
        return (name.isNotBlank())
                &&(coordinates.x > -89)
                &&(coordinates.y <= 483)
                &&(enginePower > 0)
                &&(fuelConsumption > 0)
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z")
        val stringRepresentation: StringBuilder  = StringBuilder()
            .appendLine("id: $id")
            .appendLine("name: $name")
            .appendLine("coordinates: x = ${coordinates.x}, " +
                    "y = ${coordinates.y}")
            .appendLine("creation date: ${creationDate.format(formatter)}")
            .appendLine("engine power: ${enginePower}")
            .appendLine("fuel consumption: ${fuelConsumption}")
            .appendLine("vehicle type: ${type}")
            .appendLine("fuel type: ${fuelType}")
        return stringRepresentation.toString()
    }
    constructor() : this(genId(),
        "unknown",
        Coordinates(),
        ZonedDateTime.now(),
        0.01,
        1,
        VehicleType.CHOPPER,
        FuelType.ANTIMATTER)
}


