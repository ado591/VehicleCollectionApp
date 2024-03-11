package commands

import response.Response

class Clear(): Command("clear", "очистить коллекцию") {

    override fun execute(argument: String): Response {
        //todo: any troubles with collection??
        collectionManager.clear()
        return Response("Коллекция успешно очищена")
    }
}