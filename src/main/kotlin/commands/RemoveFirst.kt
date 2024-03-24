package commands

import response.Response
import java.util.*

class RemoveFirst(): Command("remove_first",
    ResourceBundle.getBundle("message/info").getString("removeFirst_description")) {

    /**
     *  Removes first element in the collection
     *  @param argument (should be null)
     *  @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        return try {
            collectionManager.removeById(0)
            collectionManager.rearrange()
            Response("Элемент успешно удален")
        } catch (e: IndexOutOfBoundsException) {
            Response("Коллекция пуста")
        }
    }
}