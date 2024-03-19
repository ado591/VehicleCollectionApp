package commands

import response.Response

class Info(): Command("info", "вывести информацию о коллекции") {


    /**
     * @param argument a string argument (should be null)
     * @return a Response object with type, size and init time of current collection
     */
    override fun execute(argument: String?): Response {
        val message = StringBuilder()
        message.appendLine("Тип коллекции: ${collectionManager.getCollection()::class.simpleName}}")
        message.appendLine("Количество элементов: ${collectionManager.getCollection().size}")
        message.appendLine("Creation date:${collectionManager.getInitTime()}")
        return Response(message.toString())
    }
}