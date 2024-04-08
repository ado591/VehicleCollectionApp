package data

import com.fasterxml.jackson.annotation.JsonFormat
import com.fasterxml.jackson.annotation.JsonIgnoreProperties
import com.fasterxml.jackson.annotation.JsonTypeName
import java.time.ZonedDateTime
import java.time.format.DateTimeFormatter

@JsonTypeName("vehicle")
@JsonIgnoreProperties(ignoreUnknown = true)
data class Vehicle(
    var id: Long = genId(),
    var name: String,
    var coordinates: Coordinates,
    @JsonFormat(pattern = "dd-MM-yyyy HH:mm:ss z")
    var creationDate: ZonedDateTime = ZonedDateTime.now(),
    var enginePower: Double,
    var fuelConsumption: Int,
    var type: VehicleType,
    var fuelType: FuelType
) : Comparable<Vehicle>, Validatable {

    companion object {
        private var currentId: Long = 0
        fun genId(): Long {
            currentId++
            return currentId
        }

        fun setCurrentId(id: Long) {
            currentId = id
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
                && (coordinates.x > MIN_X)
                && (coordinates.y <= MAX_Y)
                && (enginePower > 0)
                && (fuelConsumption > 0)
    }

    override fun toString(): String {
        val formatter = DateTimeFormatter.ofPattern("dd-MM-yyyy HH:mm:ss z")
        val stringRepresentation: StringBuilder = StringBuilder()
            .appendLine("id: $id")
            .appendLine("name: $name")
            .appendLine(
                "coordinates: x = ${coordinates.x}, " +
                        "y = ${coordinates.y}"
            )
            .appendLine("creation date: ${creationDate.format(formatter)}")
            .appendLine("engine power: $enginePower")
            .appendLine("fuel consumption: $fuelConsumption")
            .appendLine("vehicle type: $type")
            .appendLine("fuel type: $fuelType")
        return stringRepresentation.toString()
    }

    constructor() : this(
        name = "unknown",
        coordinates = Coordinates(),
        enginePower = 0.01,
        fuelConsumption = 1,
        type = VehicleType.CHOPPER,
        fuelType = FuelType.ANTIMATTER
    )
}


