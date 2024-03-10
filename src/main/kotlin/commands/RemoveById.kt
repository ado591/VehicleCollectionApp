package commands

import response.Response
import java.util.ResourceBundle

class RemoveById(): Command("remove_by_id", "удалить элемент из коллекции по его id") {

    override fun execute(args: Array<String>): Response {
        val id: Int = TODO("Parsing input")
        return if (id >= collectionManager.getSize()) {
            Response(ResourceBundle.getBundle("error_message").getString("id_not_found"))
        } else {
            collectionManager.removeById(id)
            Response(ResourceBundle.getBundle("success_message").getString("remove"))
        }
    }

}