package commands

import response.Response
import java.util.ResourceBundle

class PrintAsc : Command( //todo: сортировка по полям
    "print_ascending",
    ResourceBundle.getBundle("message/info").getString("printAsc_description")) {

    /**
     * Sorts the elements of the original collection in natural order and creates a string representation of each element
     * Returns a Response object containing the composed message
     * @param argument a string argument (should be null)
     * @return a Response object with a message containing string representations of the sorted elements of the collection
     */
    override fun execute(argument: String?): Response {
        val message = StringBuilder()
        val sortedCollection = collectionManager.getCollection().sorted()
        for (element in sortedCollection) {
            message.appendLine(element.toString())
        }
        return Response(message.toString())
    }
}