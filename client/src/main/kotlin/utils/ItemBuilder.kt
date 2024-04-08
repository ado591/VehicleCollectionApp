package utils

import data.*
import org.koin.core.component.KoinComponent
import model.response.Response
import model.response.WarningResponse
import java.util.ResourceBundle

class ItemBuilder : KoinComponent {
    companion object {

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
            println("Введите название: ")
            do {
                val name: String? = readlnOrNull()
                if (!name.isNullOrBlank()) {
                    return name
                }
                println(ResourceBundle.getBundle("messages/error").getString("empty_line"))
            } while (true)
        }

        /**
         * adds vehicle coordinates
         */
        private fun addCoordinates(): Coordinates {
            println(Response("Введите координату Х: "))
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
                            println("Ошибка ввода. Число должно быть больше $MIN_X")
                        }
                    } ?: run {
                        println("Ошибка ввода. Введите число типа int")
                    }
                } while (true)
            }

            println(Response("Введите координату Y: "))
            run {
                do {
                    val input = readlnOrNull()
                    val number = input?.toFloatOrNull()
                    number?.let {
                        if (it <= MAX_Y) {
                            y = it
                            return@run
                        } else {
                            println("Ошибка ввода. Число не должно превосходить $MAX_Y")
                        }
                    } ?: run {
                        println(WarningResponse("Ошибка ввода. Введите число типа float"))
                    }
                } while (true)
            }
            return Coordinates(x, y)
        }

        /**
         * adds engine power
         */
        private fun addEnginePower(): Double {
            println("Введите значение engine power: ")
            do {
                val input = readlnOrNull()
                val number = input?.toDoubleOrNull()
                number?.let {
                    if (it > 0) {
                        return it
                    } else {
                        println("Ошибка ввода. Введите положительное число")
                    }
                } ?: println(WarningResponse("Ошибка ввода. Введите положительное число типа double"))
            } while (true)
        }

        /**
         * adds fuel consumption if input is valid
         * @return valid fuel consumption
         */
        private fun addFuelConsumption(): Int {
            println("Введите значение fuel consumption: ")
            do {
                val input = readlnOrNull()
                val number = input?.toIntOrNull()
                number?.let {
                    if (it > 0) {
                        return it
                    } else {
                        println("Ошибка ввода. Введите положительное число")
                    }
                } ?: println("Ошибка ввода. Введите положительное число типа int")
            } while (true)
        }

        /**
         * adds Vehicle type
         * @return vehicleType listed in VehicleType data class
         */
        private fun addVehicleType(): VehicleType {

            println("Введите тип транспортного средства. Список транспортных средств: ")
            for (value in VehicleType.entries) {
                println(Response("$value - ${value.id}"))
            }

            do {
                run {
                    val input = readln()
                    val index: Int = input.toIntOrNull() ?: let {
                        try {
                            return VehicleType.valueOf(input.uppercase())
                        } catch (e: IllegalArgumentException) {
                            println("Введенное значение не соответствует ни одному элементу enum")
                            return@run
                        }
                    }
                    return getTypeById(index) ?: let {
                        println("Нет элемента с таким id")
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
            println("Введите тип топлива. Список видов топлива: ")
            for (value in FuelType.entries) {
                println("$value - ${value.id}")
            }
            do {
                run {
                    val input = readln()
                    val index: Int = input.toIntOrNull() ?: let {
                        try {
                            return FuelType.valueOf(input.uppercase())
                        } catch (e: IllegalArgumentException) {
                            println("Введенное значение не соответствует ни одному элементу enum")
                            return@run
                        }
                    }
                    return getFuelTypeById(index) ?: let {
                        println("Нет элемента с таким id")
                        return@run
                    }
                }
            } while (true)
        }

        /* оно теперь живет внутри Autogeneretable
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
        }*/
    }

}