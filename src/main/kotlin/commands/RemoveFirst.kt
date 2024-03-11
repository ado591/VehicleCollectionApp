package commands

import response.Response

class RemoveFirst(): Command("remove_first", "удалить первый элемент из коллекции") {

    override fun execute(argument: String): Response {
        return try {
            collectionManager.removeById(0)
            Response("Элемент успешно удален")
        } catch (e: IndexOutOfBoundsException) {
            Response("Коллекция пуста")
        }
    }
}