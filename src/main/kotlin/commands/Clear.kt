package commands

import response.Response
import java.util.ResourceBundle

class Clear : Command(
    "clear",
    ResourceBundle.getBundle("message/info").getString("clear_description")) {

    /**
     * Clears the collection
     * @param argument (should be null)
     * @return a Response object containing a messages with result of command
     */
    override fun execute(argument: String?): Response {
        collectionManager.clear()
        return Response("Коллекция успешно очищена")
    }
}