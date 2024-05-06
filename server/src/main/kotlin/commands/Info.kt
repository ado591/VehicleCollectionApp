package commands

import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class Info : Command(
    "info",
    ResourceBundle.getBundle("message/info").getString("info_description")
) {


    /**
     * @param argument a string argument (should be null)
     * @return a Response object with type, size and init time of current collection
     */
    override fun execute(argument: String?): Response {
        logger.info("Creating info message about current collection")
        val message = StringBuilder()
        message.appendLine("Тип коллекции: ${collectionManager.getCollection()::class.simpleName}")
        message.appendLine("Количество элементов: ${collectionManager.getCollection().size}")
        message.appendLine("Creation date: ${collectionManager.getInitTime()}")
        return Response(message.toString()).apply { responseType = ResponseType.SUCCESS }
    }
}