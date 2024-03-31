package commands

import data.VehicleType
import response.Response
import java.util.ResourceBundle
import kotlin.IllegalArgumentException

class RemoveByType : Command(
    "remove_all_by_type",
    ResourceBundle.getBundle("message/info").getString("removeByType_description")
) {

    override fun execute(argument: String?): Response { //todo: тип по ordinal?
        val removingType: VehicleType = argument?.let {
            try {
                VehicleType.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                return Response("Такого типа не существует")
            }
        } ?: return Response("Не передан тип для удаления")
        val sizeBeforeExecute = collectionManager.getSize()
        collectionManager.getCollection().removeIf { it.type == removingType }
        collectionManager.rearrange()
        return if (sizeBeforeExecute == collectionManager.getSize()) {
            Response("Нет элементов, удовлетворяющих условиям поиска")
        } else {
            Response("Элементы типа $removingType удалены")
        }
    }
}