package commands.extra

import console.Console
import data.Coordinates
import data.FuelType
import data.Vehicle
import data.VehicleType
import managers.CollectionManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.time.ZonedDateTime
import kotlin.random.Random

class ItemBuilder(): KoinComponent {
    // todo: к enum-ам обращаться в том числе по числу. Можно использовать ordinal
    private val collectionManager: CollectionManager by inject()
    private val console: Console by inject()

    /**
     * build a valid Vehicle element
     * @return a valid Vehicle element
     */
    fun consoleAdd(): Vehicle {
        val id = (collectionManager.getSize() + 1).toLong()
        val creationDate: ZonedDateTime = ZonedDateTime.now()
        return Vehicle(
            id,
            addName(),
            addCoordinates(),
            creationDate,
            addEnginePower(),
            addFuelConsumption(),
            addVehicleType(),
            addFuelType()
        )
    }

    /**
     * adds vehicle name
     */
    private fun addName(): String {
        console.print(Response("Введите название: "))
        do {
            try {
                val name: String = readln()
                if (name.isNotBlank()) {  // тут проверка не на пустоту, а на наличие видимых символов
                    return name
                } else {
                    console.print(Response("Строка не должна быть пустой!"))
                }
            } catch (e: NullPointerException) {
                console.print(Response("Поле не может быть null!"))
            }
        } while (true)
    }

    /**
     * adds vehicle coordinates
     */
    private fun addCoordinates(): Coordinates {
        console.print(Response("Введите координату Х: "))
        var x: Int
        var y: Float
        do {
            try {
                val input = readlnOrNull()
                val number = input?.toInt()
                if (number != null && number > -89) {
                    x = number
                    break
                } else {
                    throw NumberFormatException()
                }
            } catch (e: NumberFormatException) {
                console.print(Response("Ошибка ввода. Введите число типа int, которое больше -89"))
            }
        } while (true)

        console.print(Response("Введите координату Y: "))
        do {
            try {
                val input = readlnOrNull()
                val number = input?.toFloat()
                if (number != null && number <= 483) {
                    y = number
                    break
                } else {
                    throw NumberFormatException()
                }
            } catch (e: NumberFormatException) {
                console.print(Response("Ошибка ввода. Введите число типа float, которое не превосходит 483"))
            }
        } while (true)
        return Coordinates(x, y)
    }

    /**
     * adds engine power
     */
    private fun addEnginePower(): Double {
        var enginePower: Double
        console.print(Response("Введите значение engine power: "))
        do {
            try {
                val input = readlnOrNull()
                val number = input?.toDouble()
                if (number != null && number > 0) {
                    enginePower = number
                    return enginePower
                } else {
                    throw NumberFormatException()
                }
            } catch (e: NumberFormatException) {
                console.print(Response("Ошибка ввода. Введите положительное число типа double"))
            }
        } while (true)
    }

    /**
     * adds fuel consumption if input is valid
     * @return valid fuel consumption
     */
    private fun addFuelConsumption(): Int {
        var fuelConsumption: Int
        console.print(Response("Введите значение fuel consumption: "))
        do {
            try {
                val input = readlnOrNull()
                val number = input?.toInt()
                if (number != null && number > 0) {
                    fuelConsumption = number
                    return fuelConsumption
                } else {
                    throw NumberFormatException()
                }
            } catch (e: NumberFormatException) {
                console.print(Response("Ошибка ввода. Введите положительное число типа int"))
            }
        } while (true)
    }


    /**
     * adds Vehicle type
     * @return vehicleType listed in VehicleType data class
     */
    private fun addVehicleType(): VehicleType {
        console.print(Response("Введите тип транспортного средства. Список транспортных средств: "))
        for (value in VehicleType.entries) {
            console.print(Response(value.toString()))
        }
        do {
            try {
                val input = readln()
                return VehicleType.valueOf(input)
            } catch (e: IllegalArgumentException) {
                console.print(Response("Введенное значение не соответствует ни одному элементу enum"))
            }
        } while (true)
    }

    /**
     * Adds fuel type
     * @return fuelType listed in FuelType data class
     */
    private fun addFuelType(): FuelType {
        console.print(Response("Введите тип топлива. Список видов топлива: "))
        for (value in FuelType.entries) {
            console.print(Response(value.toString()))
        }
        do {
            try {
                val input = readln()
                return FuelType.valueOf(input)
            } catch (e: IllegalArgumentException) {
                console.print(Response("Введенное значение не соответствует ни одному элементу enum"))
            }
        } while (true)
    }

    /**
     * Automatically generates a valid item for Vehicle collection
     */
    fun autoAdd(): Vehicle { // todo: сделать фабричный метод??
        // todo: делать что-то с magic numbers??
       val id = collectionManager.getSize().toLong()
        val name = "Autogenerated item"
        val coordinates = Coordinates(Random.nextInt(-88, Int.MAX_VALUE), Random.nextFloat() * 484)
        val creationDate = ZonedDateTime.now()
        val enginePower = Random.nextDouble() * Double.MAX_VALUE
        val fuelConsumption = Random.nextInt(1, Int.MAX_VALUE)
        val type = VehicleType.entries.toTypedArray().random()
        val fuelType = FuelType.entries.toTypedArray().random()
        return Vehicle(id, name, coordinates, creationDate, enginePower, fuelConsumption, type, fuelType)
    }

}