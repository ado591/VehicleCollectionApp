package commands

import model.User
import model.response.Response
import model.response.ResponseType
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
    override fun execute(argument: String?, user: User): Response {
        return if (collectionManager.isEmpty()) {
            logger.info("Trying to process command ${name()} with an empty collection")
            Response("Если у вас нет машины, то и топлива у нее нет").apply { responseType = ResponseType.WARNING }
        } else {
            val sumOfFuel = collectionManager.getCollection().sumOf { it.fuelConsumption }
            val message = "fuel sum: $sumOfFuel"
            logger.info("Required fuel sum equals $sumOfFuel")
            Response(message).apply { responseType = ResponseType.SUCCESS }
        }
    }
}