package commands

import response.Response

class RemoveById(): Command("remove_by_id", "удалить элемент из коллекции по его id") {

    /**
     * Removes item by given id iff argument represents index in current collection
     * @param argument a string argument representing the ID of the element to be removed
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        val id: Int
        return try {
            id = argument!!.toInt()
            collectionManager.removeById(id)
            Response("Элемент успешно удален")
        } catch (e: NumberFormatException) {
            Response("Аргумент команды должен быть числом")
        } catch (e: IndexOutOfBoundsException) {
            Response("Указан некорректный индекс")
        } catch(e: NullPointerException) {
            Response("Не передан индекс")
        }
    }

}