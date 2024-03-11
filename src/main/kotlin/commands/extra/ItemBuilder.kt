package commands.extra

import console.Console
import data.Coordinates
import data.FuelType
import data.Vehicle
import data.VehicleType
import managers.CollectionManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.time.ZonedDateTime
import kotlin.random.Random

class ItemBuilder(): KoinComponent {
    // todo: к enum-ам обращаться в том числе по числу. Можно использовать ordinal
    private val collectionManager: CollectionManager by inject()
    private val console: Console by inject()

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

    private fun addName(): String {
        console.print("Введите название: ")
        do {
            try {
                val name: String = readln()
                if (name.isNotBlank()) {  // тут проверка не на пустоту, а на наличие видимых символов
                    return name
                } else {
                    console.print("Строка не должна быть пустой!")
                }
            } catch (e: NullPointerException) {
                console.print("Поле не может быть null!")
            }
        } while (true)
    }

    private fun addCoordinates(): Coordinates {
        console.print("Введите координату Х: ")
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
                console.print("Ошибка ввода. Введите число типа int, которое больше -89")
            }
        } while (true)

        console.print("Введите координату Y: ")
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
                console.print("Ошибка ввода. Введите число типа float, которое не превосходит 483")
            }
        } while (true)
        return Coordinates(x, y)
    }

    private fun addEnginePower(): Double {
        var enginePower: Double
        console.print("Введите значение engine power: ")
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
                console.print("Ошибка ввода. Введите положительное число типа double")
            }
        } while (true)
    }

    private fun addFuelConsumption(): Int {
        var fuelConsumption: Int
        console.print("Введите значение fuel consumption: ")
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
                console.print("Ошибка ввода. Введите положительное число типа int")
            }
        } while (true)
    }

    private fun addVehicleType(): VehicleType {
        console.print("Введите тип транспортного средства. Список транспортных средств: ")
        for (value in VehicleType.entries) {
            console.print(value.toString())
        }
        do {
            try {
                val input = readln()
                return VehicleType.valueOf(input)
            } catch (e: IllegalArgumentException) {
                println("Введенное значение не соответствует ни одному элементу enum")
            }
        } while (true)
    }

    private fun addFuelType(): FuelType {
        console.print("Введите тип топлива. Список видов топлива: ")
        for (value in FuelType.entries) {
            console.print(value.toString())
        }
        do {
            try {
                val input = readln()
                return FuelType.valueOf(input)
            } catch (e: IllegalArgumentException) {
                println("Введенное значение не соответствует ни одному элементу enum")
            }
        } while (true)
    }

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