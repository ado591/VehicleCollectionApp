package commands

import commands.extra.ItemBuilder
import response.Response

class UpdateId(): Command("update_id", "обновить значение элемента коллекции, id которого равен заданному") {

    override fun execute(argument: String): Response {
        return try {
            val id: Int = argument.toInt()
            val newElement = ItemBuilder().consoleAdd()
            collectionManager.update(id, newElement)
            Response("Элемент успешно обновлен")
        } catch (e: NumberFormatException) {
            Response("В качестве аргумента должно быть натуральное число")
        } catch (e: IndexOutOfBoundsException) {
            Response("Введенный индекс не в границах")
        }
    }
}