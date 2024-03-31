package commands.extra

import console.Console
import data.*
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.util.ResourceBundle
import kotlin.random.Random

class ItemBuilder : KoinComponent {
    private val console: Console by inject()

    /**
     * build a valid Vehicle element
     * @return a valid Vehicle element
     */
    fun consoleAdd(): Vehicle {
        return Vehicle(
            name = addName(),
            coordinates = addCoordinates(),
            enginePower = addEnginePower(),
            fuelConsumption = addFuelConsumption(),
            type = addVehicleType(),
            fuelType = addFuelType()
        )
    }

    /**
     * adds vehicle name
     */
    private fun addName(): String {
        console.print(Response("Введите название: "))
        do {
            val name: String? = readlnOrNull()
            if (name.isNullOrBlank()) {
                console.print(Response(ResourceBundle.getBundle("messages/error").getString("empty_line")))
            } else {
                return name
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
        run {
            do {
                val input = readlnOrNull()
                val number = input?.toIntOrNull()
                number?.let {
                    if (it > MIN_X) {
                        x = it
                        return@run
                    } else {
                        console.print(Response("Ошибка ввода. Число должно быть больше $MIN_X"))
                    }
                } ?: run {
                    console.print(Response("Ошибка ввода. Введите число типа int"))
                }
            } while (true)
        }

        console.print(Response("Введите координату Y: "))
        run {
            do {
                val input = readlnOrNull()
                val number = input?.toFloatOrNull()
                number?.let {
                    if (it <= MAX_Y) {
                        y = it
                        return@run
                    } else {
                        console.print(Response("Ошибка ввода. Число не должно превосходить $MAX_Y"))
                    }
                } ?: run {
                    console.print(Response("Ошибка ввода. Введите число типа float"))
                }
            } while (true)
        }
        return Coordinates(x, y)
    }

    /**
     * adds engine power
     */
    private fun addEnginePower(): Double {
        console.print(Response("Введите значение engine power: "))
        do {
            val input = readlnOrNull()
            val number = input?.toDoubleOrNull()
            number?.let {
                if (it > 0) {
                    return it
                } else {
                    console.print(Response("Ошибка ввода. Введите положительное число"))
                }
            } ?: console.print(Response("Ошибка ввода. Введите положительное число типа double"))
        } while (true)
    }

    /**
     * adds fuel consumption if input is valid
     * @return valid fuel consumption
     */
    private fun addFuelConsumption(): Int {
        console.print(Response("Введите значение fuel consumption: "))
        do {
            val input = readlnOrNull()
            val number = input?.toIntOrNull()
            number?.let {
                if (it > 0) {
                    return it
                } else {
                    console.print(Response("Ошибка ввода. Введите положительное число"))
                }
            } ?: console.print(Response("Ошибка ввода. Введите положительное число типа int"))
        } while (true)
    }

    /**
     * adds Vehicle type
     * @return vehicleType listed in VehicleType data class
     */
    private fun addVehicleType(): VehicleType {

        console.print(Response("Введите тип транспортного средства. Список транспортных средств: "))
        for (value in VehicleType.entries) {
            console.print(Response("$value - ${value.id}"))
        }

        do {
            run {
                val input = readln()
                val index: Int = input.toIntOrNull() ?: let {
                    try {
                        return VehicleType.valueOf(input.uppercase())
                    } catch (e: IllegalArgumentException) {
                        console.print(Response("Введенное значение не соответствует ни одному элементу enum"))
                        return@run
                    }
                }
                return getTypeById(index) ?: let {
                    console.print(Response("Нет элемента с таким id"))
                    return@run
                }
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
            console.print(Response("$value - ${value.id}"))
        }
        do {
            run {
                val input = readln()
                val index: Int = input.toIntOrNull() ?: let {
                    try {
                        return FuelType.valueOf(input.uppercase())
                    } catch (e: IllegalArgumentException) {
                        console.print(Response("Введенное значение не соответствует ни одному элементу enum"))
                        return@run
                    }
                }
                return getFuelTypeById(index) ?: let {
                    console.print(Response("Нет элемента с таким id"))
                    return@run
                }
            }
        } while (true)
    }


    /**
     * Automatically generates a valid item for Vehicle collection
     */
    fun autoAdd(): Vehicle {
        val name = "Autogenerated item"
        val coordinates = Coordinates(Random.nextInt(-88, Int.MAX_VALUE), Random.nextFloat() * 484)
        val enginePower = Random.nextDouble() * Double.MAX_VALUE
        val fuelConsumption = Random.nextInt(1, Int.MAX_VALUE)
        val type = VehicleType.entries.toTypedArray().random()
        val fuelType = FuelType.entries.toTypedArray().random()
        return Vehicle(
            name = name,
            coordinates = coordinates,
            enginePower = enginePower,
            fuelConsumption = fuelConsumption,
            type = type,
            fuelType = fuelType
        )
    }

}