package commands

import model.response.Response
import model.response.SuccessResponse
import model.response.WarningResponse
import java.util.ResourceBundle

class SumOfFuel : Command(
    "sum_of_fuel_consumption",
    ResourceBundle.getBundle("message/info").getString("sumOfFuel_description")
) {

    /**
     * Prints sum of fuel for all vehicles listed in collection
     * @param argument (should be null)
     * @return a Response object with a sum of fuel or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        return if (collectionManager.isEmpty()) {
            WarningResponse("Если у вас нет машины, то и топлива у нее нет")
        } else {
            val sumOfFuel = collectionManager.getCollection().sumOf { it.fuelConsumption }
            val message = "fuel sum: $sumOfFuel"
            SuccessResponse(message)
        }
    }
}