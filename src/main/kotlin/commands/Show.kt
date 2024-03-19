package commands

import response.Response
import java.util.* // todo: no .* imports

class Show(): Command("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении") {

    /**
     * Prints all elements in collection in String format
     * @param argument (should be null)
     * @return a Response object which contains a list of all elements in collection in string format
     */
    override fun execute(argument: String?): Response {
        val message = StringBuilder("Элементы коллекции в строковом представлении:\n")
        for (elem in collectionManager.getCollection()) {
            message.appendLine(elem.toString())
        }
        return Response(message.toString())
    }
}