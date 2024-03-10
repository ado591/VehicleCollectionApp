package commands

import response.Response
import java.util.ResourceBundle

class UpdateId(): Command("update_id", "обновить значение элемента коллекции, id которого равен заданному") {

    override fun execute(args: Array<String>): Response {
        val id: Int = args[0].toInt() // todo: check if input is integer, remove magic number
        return if (id >= collectionManager.getSize() || id < 0) { //todo: мб заменить на validateId?
            Response(ResourceBundle.getBundle("error_message").getString("invalid_id"))
        } else {
            Response(ResourceBundle.getBundle("success_message").getString("update"))
        }
    }
}