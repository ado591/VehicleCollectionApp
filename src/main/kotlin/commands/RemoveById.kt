package commands

import response.Response
import java.util.ResourceBundle

class RemoveById(): Command("remove_by_id", "удалить элемент из коллекции по его id") {

    override fun execute(args: Array<String>?): Response {
        val id: Int
        return try {
            id = args[0].toInt() //todo: подумать о null
            collectionManager.removeById(id)
            Response("Элемент успешно удален")
        } catch (e: NumberFormatException) { //todo: обработать null
            Response("Аргумент команды должен быть числом")
        } catch (e: IndexOutOfBoundsException) {
            Response("Указан некорректный индекс")
        }
    }

}