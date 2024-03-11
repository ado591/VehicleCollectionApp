package commands

import response.Response
import java.util.ResourceBundle

class RemoveFirst(): Command("remove_first", "удалить первый элемент из коллекции") {

    override fun execute(args: Array<String>?): Response {
        return if (collectionManager.isEmpty()) {
            Response(ResourceBundle.getBundle("error_message").getString("empty_collection"))
        } else {
            collectionManager.removeById(1) //todo: fix magic number
            Response(ResourceBundle.getBundle("success_message").getString("remove"))
        }
    }
}