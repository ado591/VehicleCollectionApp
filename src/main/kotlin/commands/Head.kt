package commands

import response.Response
import java.util.ResourceBundle

class Head(): Command("head", "вывести первый элемент коллекции") {

    override fun execute(args: Array<String>): Response {
        return if (collectionManager.isEmpty()) {
            Response(ResourceBundle.getBundle("error_message").getString("empty_collection"))
        } else {
            val message: StringBuilder =
                StringBuilder(ResourceBundle.getBundle("success_message").getString("head"))
            message.append(" - ${collectionManager.getById(0)}") //todo: magic number
            Response(message.toString())
        }
    }
}