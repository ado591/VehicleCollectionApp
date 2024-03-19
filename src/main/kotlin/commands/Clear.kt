package commands

import response.Response

class Clear(): Command("clear", "очистить коллекцию") {

    /**
     * Clears the collection
     * @param argument (should be null)
     * @return a Response object containing a messages with result of command
     */
    override fun execute(argument: String?): Response {
        //todo: any troubles with collection??
        return try {
            collectionManager.clear()
            Response("Коллекция успешно очищена")
        } catch (e: Exception) {
            Response("Неизвестная ошибка при выполнении команды ${this.name()}")
        }
    }
}