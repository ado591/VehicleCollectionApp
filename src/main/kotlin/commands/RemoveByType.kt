package commands

import data.VehicleType
import response.Response
import java.lang.IllegalArgumentException
import java.util.*

class RemoveByType(): Command("remove_all_by_type",
    ResourceBundle.getBundle("message/info").getString("removeByType_description")) {

    override fun execute(argument: String?): Response {
        val removingType = try {
            VehicleType.valueOf(argument!!.uppercase())
        } catch (e: NullPointerException) {
            return Response("Не передан тип для удаления")
        } catch (e: IllegalArgumentException) {
            return Response("Такого типа не существует")
        }
        val sizeBeforeExecute = collectionManager.getSize()
        collectionManager.getCollection().removeIf{it.type == removingType}
        collectionManager.rearrange()
        return if (sizeBeforeExecute == collectionManager.getSize()) {
            Response("Нет элементов, удовлетворяющих условиям поиска")
        } else{
            Response("Элементы типа $removingType удалены")
        }
    }
}