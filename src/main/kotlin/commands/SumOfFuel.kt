package commands

import response.Response
import java.util.ResourceBundle

class SumOfFuel(): Command("sum_of_fuel_consumption", "вывести сумму значений поля fuelConsumption для всех элементов коллекции") {

    override fun execute(args: Array<String>): Response {
        return if (collectionManager.isEmpty()) {
            Response(ResourceBundle.getBundle("error_message").getString("empty_collection"))
        } else {
            val sumOfFuel = collectionManager.getCollection().sumOf { it.getFuelConsumption() }
            val message = StringBuilder(ResourceBundle.getBundle("info_message").getString("fuel_sum"))
            message.append(" - $sumOfFuel")
            Response(message.toString())
        }
    }
}