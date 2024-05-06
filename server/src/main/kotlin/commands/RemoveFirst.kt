package commands

import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class RemoveFirst : Command(
    "remove_first",
    ResourceBundle.getBundle("message/info").getString("removeFirst_description")
) {

    /**
     *  Removes first element in the collection
     *  @param argument (should be null)
     *  @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        return if (collectionManager.isEmpty()) {
            logger.warn("Trying to process command with an empty collection")
            Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        } else {
            collectionManager.removeById(0)
            collectionManager.rearrange()
            logger.info("First element was removed from the collection")
            Response("Элемент успешно удален").apply { responseType = ResponseType.SUCCESS }
        }
    }
}