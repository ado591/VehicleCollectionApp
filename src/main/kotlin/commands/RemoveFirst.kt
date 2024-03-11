package commands

import response.Response
import java.util.ResourceBundle

class RemoveFirst(): Command("remove_first", "удалить первый элемент из коллекции") {

    override fun execute(args: Array<String>?): Response {
        return try {
            collectionManager.removeById(0)
            Response("Элемент успешно удален")
        } catch (e: IndexOutOfBoundsException) {
            Response("Коллекция пуста")
        }
    }
}