package commands

import response.Response
import java.util.*

class Head(): Command("head",
    ResourceBundle.getBundle("message/info").getString("head_description")) {

    /**
     * Retrieves the first element of the collection using collectionManager.head() method if first element exists
     * @param argument a string argument (should be null)
     * @return a Response object with the result of attempting to retrieve the first element of the collection
     */
    override fun execute(argument: String?): Response {
        return try {
            Response("Первый элемент коллекции: ${collectionManager.head()}")
        } catch (e: IndexOutOfBoundsException) {
            Response("Коллекция пуста")
        }
    }
}