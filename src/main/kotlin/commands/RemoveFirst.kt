package commands

import response.Response

class RemoveFirst(): Command("remove_first", "удалить первый элемент из коллекции") {

    /**
     *  Removes first element in the collection
     *  @param argument (should be null)
     *  @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        return try {
            collectionManager.removeById(0)
            Response("Элемент успешно удален")
        } catch (e: IndexOutOfBoundsException) {
            Response("Коллекция пуста")
        }
    }
}