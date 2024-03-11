package commands

import response.Response
import java.util.ResourceBundle

class Head(): Command("head", "вывести первый элемент коллекции") {

    override fun execute(args: Array<String>?): Response {
        return try {
            val message: StringBuilder =
                StringBuilder(ResourceBundle.getBundle("success_message").getString("head"))
            message.append(" - ${collectionManager.head()}")
            Response(message.toString())
        } catch (e: IndexOutOfBoundsException) {
            Response(ResourceBundle.getBundle("error_message").getString("empty_collection"))
        }
    }
}