package commands

import model.User
import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class Head : Command(
    "head",
    ResourceBundle.getBundle("message/info").getString("head_description")
) {

    /**
     * Retrieves the first element of the collection using collectionManager.head() method if first element exists
     * @param argument a string argument (should be null)
     * @return a Response object with the result of attempting to retrieve the first element of the collection
     */
    override fun execute(argument: String?, user: User): Response {
        return if (collectionManager.isEmpty()) {
            logger.warn("Collection is empty")
            Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        } else {
            logger.info("Printing first element of the collection")
            Response("Первый элемент коллекции: ${collectionManager.head()}").apply {
                responseType = ResponseType.SUCCESS
            }
        }
    }
}