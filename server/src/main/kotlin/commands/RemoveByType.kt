package commands

import data.VehicleType
import exceptions.InvalidArgumentException
import model.response.Response
import model.response.SuccessResponse
import model.response.WarningResponse
import java.util.ResourceBundle
import kotlin.IllegalArgumentException

class RemoveByType : Command(
    "remove_all_by_type",
    ResourceBundle.getBundle("message/info").getString("removeByType_description")
) {

    override fun execute(argument: String?): Response {
        val removingType: VehicleType = argument?.let {
            try {
                VehicleType.valueOf(it.uppercase())
            } catch (e: IllegalArgumentException) {
                throw InvalidArgumentException("Такого типа не существует")
            }
        } ?: throw InvalidArgumentException("Не передан тип для удаления")
        val sizeBeforeExecute = collectionManager.getSize()
        collectionManager.getCollection().removeIf { it.type == removingType }
        collectionManager.rearrange()
        return if (sizeBeforeExecute == collectionManager.getSize()) {
            WarningResponse("Нет элементов, удовлетворяющих условиям поиска")
        } else {
            SuccessResponse("Элементы типа $removingType удалены")
        }
    }
}