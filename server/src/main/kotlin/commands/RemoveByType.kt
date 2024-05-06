package commands

import data.VehicleType
import exceptions.InvalidArgumentException
import model.response.Response
import model.response.ResponseType
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
                logger.error("Could not find $argument enum")
                throw InvalidArgumentException("Такого типа не существует")
            }
        } ?: throw InvalidArgumentException("Не передан тип для удаления")
        val sizeBeforeExecute = collectionManager.getSize()
        collectionManager.getCollection().removeIf { it.type == removingType }
        collectionManager.rearrange()
        return if (sizeBeforeExecute == collectionManager.getSize()) {
            logger.warn("No item with type $removingType was found in the collection")
            Response("Нет элементов, удовлетворяющих условиям поиска").apply { responseType = ResponseType.WARNING }
        } else {
            logger.info("All items with type $removingType were removed")
            Response("Элементы типа $removingType удалены").apply { responseType = ResponseType.SUCCESS }
        }
    }
}