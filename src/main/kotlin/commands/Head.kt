package commands

import response.Response

class Head(): Command("head", "вывести первый элемент коллекции") {

    override fun execute(argument: String): Response {
        return try {
            val message: StringBuilder =
                StringBuilder("Первый элемент коллекции")
            message.append(" - ${collectionManager.head()}")
            Response(message.toString())
        } catch (e: IndexOutOfBoundsException) {
            Response("Коллекция пуста")
        }
    }
}