package commands

import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class PrintAsc : Command(
    "print_ascending",
    ResourceBundle.getBundle("message/info").getString("printAsc_description")
) {

    /**
     * Sorts the elements of the original collection in natural order and creates a string representation of each element
     * Returns a Response object containing the composed message
     * @param argument a string argument (should be null)
     * @return a Response object with a message containing string representations of the sorted elements of the collection
     */
    override fun execute(argument: String?): Response {
        if (collectionManager.isEmpty()) {
            logger.warn("Collection is empty")
            return Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        }
        val message = collectionManager.getCollection()
            .sorted()
            .joinToString("\n") { it.toString() }
        return Response(message).apply { responseType = ResponseType.SUCCESS }
    }
}