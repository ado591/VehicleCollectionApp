package commands

import data.Vehicle
import model.User
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
    override fun execute(argument: String?, user: User): Response {
        for (vehicle in collectionManager.getCollection()) {
            if (!dbManager.checkCreator(vehicle.id, user)) {
                logger.error("Trying to clear collection without required permission")
                return Response("У вас нет прав для модификации данного объекта").apply {
                    responseType = ResponseType.ERROR
                }
            }
        }
        dbManager.clearVehicles()
        logger.info("Database was cleared")
        collectionManager.clear()
        Vehicle.setCurrentId(0)
        logger.info("Collection was cleared")
        return Response("Коллекция успешно очищена").apply { responseType = ResponseType.SUCCESS }
    }
}