package commands

import data.Vehicle
import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class Clear : Command(
    "clear",
    ResourceBundle.getBundle("message/info").getString("clear_description")
) {

    /**
     * Clears the collection
     * @param argument (should be null)
     * @return a Response object containing a messages with result of command
     */
    override fun execute(argument: String?): Response {
        collectionManager.clear()
        Vehicle.setCurrentId(0)
        logger.info("Collection was cleared")
        return Response("Коллекция успешно очищена").apply { responseType = ResponseType.SUCCESS }
    }
}