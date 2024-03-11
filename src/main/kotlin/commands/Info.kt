package commands

import response.Response

class Info(): Command("info", "вывести информацию о коллекции") {

    override fun execute(argument: String): Response {
        val message = StringBuilder()
        message.append("Тип коллекции: ${collectionManager.getCollection()::class.simpleName}}\n")
        message.append("Количество элементов: ${collectionManager.getCollection().size}\n")
        message.append("Creation date:${collectionManager.getInitTime()}\n")
        return Response(message.toString())
    }
}