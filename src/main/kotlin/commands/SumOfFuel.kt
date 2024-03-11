package commands

import response.Response

class SumOfFuel(): Command("sum_of_fuel_consumption", "вывести сумму значений поля fuelConsumption для всех элементов коллекции") {

    override fun execute(argument: String): Response {
        return if (collectionManager.isEmpty()) {
            Response("Если у вас нет машины, то и топлива у нее нет")
        } else {
            val sumOfFuel = collectionManager.getCollection().sumOf { it.getFuelConsumption() }
            val message = "fuel sum: $sumOfFuel"
            Response(message)
        }
    }
}