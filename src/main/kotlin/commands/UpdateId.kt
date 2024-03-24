package commands

import commands.extra.ItemBuilder
import response.Response
import java.util.*

class UpdateId(): Command("update_id",
    ResourceBundle.getBundle("message/info").getString("updateId_description")) {

    /**
     * Updates element with given ID in the collection
     * @param argument ID to update
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response { //todo: добавить автоген
        return try {
            val id: Int = argument?.toInt()!!
            if (collectionManager.isEmpty()) {
                return Response("Коллекция пуста")
            } else if (id <= 0 || id > collectionManager.getSize()) {
                return Response("Указан некорректный индекс")
            }
            val newElement = ItemBuilder().consoleAdd()
            collectionManager.update(id - 1, newElement)
            Response("Элемент успешно обновлен")
        } catch (e: NumberFormatException) {
            Response("В качестве аргумента должно быть натуральное число")
        } catch (e: Exception) {
            Response("Неизвестная ошибка")
        }
    }
}