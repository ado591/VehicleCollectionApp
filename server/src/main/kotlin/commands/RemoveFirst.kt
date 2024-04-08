package commands

import model.response.Response
import model.response.SuccessResponse
import model.response.WarningResponse
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
            WarningResponse("Коллекция пуста")
        } else {
            collectionManager.removeById(0)
            collectionManager.rearrange()
            SuccessResponse("Элемент успешно удален")
        }
    }
}