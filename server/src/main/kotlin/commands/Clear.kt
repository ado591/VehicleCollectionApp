package commands

import data.Vehicle
import model.response.Response
import model.response.SuccessResponse
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
        return SuccessResponse("Коллекция успешно очищена")
    }
}