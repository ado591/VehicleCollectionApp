package commands

import commands.extra.ItemBuilder
import response.Response
import java.util.ResourceBundle

class UpdateId(): Command("update_id", "обновить значение элемента коллекции, id которого равен заданному") {

    override fun execute(args: Array<String>?): Response {
        return try {
            val id: Int = args[0].toInt() // todo: подумать про null
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