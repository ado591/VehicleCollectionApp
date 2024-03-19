package commands

import commands.extra.ItemBuilder
import response.Response

class UpdateId(): Command("update_id", "обновить значение элемента коллекции, id которого равен заданному") {

    /**
     * Updates element with given ID in the collection
     * @param argument ID to update
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        return try {
            val id: Int = argument?.toInt() ?: "бочка бас качает соло".toInt()
            val newElement = ItemBuilder().consoleAdd()
            collectionManager.update(id, newElement)
            Response("Элемент успешно обновлен")
        } catch (e: NumberFormatException) {
            Response("В качестве аргумента должно быть натуральное число")
        } catch (e: IndexOutOfBoundsException) {
            Response("Указан некорректный индекс ")
        }
    }
}