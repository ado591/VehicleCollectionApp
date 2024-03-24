package commands

import response.Response
import java.util.* // todo: no .* imports

class Show(): Command("show",
    ResourceBundle.getBundle("message/info").getString("show_description")) {

    /**
     * Prints all elements in collection in String format
     * @param argument (should be null)
     * @return a Response object which contains a list of all elements in collection in string format
     */
    override fun execute(argument: String?): Response {
        if (collectionManager.isEmpty()) {
            return Response("Коллекция пуста")
        }
        val message = StringBuilder()
            .appendLine("Элементы коллекции в строковом представлении:")
        for (elem in collectionManager.getCollection()) {
            message.appendLine(elem.toString())
        }
        return Response(message.toString())
    }
}