package commands

import exceptions.InvalidArgumentException
import response.Response
import java.util.ResourceBundle

class RemoveById : Command(
    "remove_by_id",
    ResourceBundle.getBundle("message/info").getString("removeById_description")
) {

    /**
     * Removes item by given id iff argument represents index in current collection
     * @param argument a string argument representing the ID of the element to be removed
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        val id: Int = (argument?.let {
            it.toIntOrNull()
                ?: throw InvalidArgumentException("Аргумент команды должен быть числом")
        }
            ?: throw InvalidArgumentException("Не передан индекс")) - 1
        return if (collectionManager.inBounds(id)) {
            collectionManager.removeById(id)
            collectionManager.rearrange(id)
            Response("Элемент успешно удален")
        } else {
            Response("Указан некорректный индекс")
        }
    }
}