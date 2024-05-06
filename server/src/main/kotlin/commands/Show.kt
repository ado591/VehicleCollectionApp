package commands

import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle


class Show : Command(
    "show",
    ResourceBundle.getBundle("message/info").getString("show_description")
) {

    /**
     * Prints all elements in collection in String format
     * @param argument (should be null)
     * @return a Response object which contains a list of all elements in collection in string format
     */
    override fun execute(argument: String?): Response {
        if (collectionManager.isEmpty()) {
            logger.warn("Trying to process command ${name()} with an empty collection")
            return Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        }
        val message = StringBuilder()
            .appendLine("Элементы коллекции в строковом представлении:")
        collectionManager.getCollection()
            .sorted()
            .forEach { elem -> message.appendLine(elem.toString()) }
        logger.info("A list with the whole collection was created")
        return Response(message.toString()).apply { responseType = ResponseType.SUCCESS }
    }
}