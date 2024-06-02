package commands

import exceptions.InvalidArgumentException
import exceptions.users.UserNotAuthorizedException
import model.User
import model.response.Response
import model.response.ResponseType
import java.sql.SQLException
import java.util.ResourceBundle

class RemoveById : Command(
    "remove_by_id",
    ResourceBundle.getBundle("message/info").getString("removeById_description")
) {

    /**
     * Removes item by given id iff argument represents index in current collection
     * @param argument a string argument representing the ID of the element to be removed
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?, user: User?): Response {
        val id: Int = (argument?.let {
            it.toIntOrNull()
                ?: throw InvalidArgumentException("Аргумент команды должен быть числом")
        }
            ?: throw InvalidArgumentException("Не передан индекс")) - 1
        return if (collectionManager.inBounds(id)) {
            if (!dbManager.checkCreator(id.toLong(), user ?: throw UserNotAuthorizedException())) {
                return Response("У вас нет прав для модификации данного объекта").apply {
                    responseType = ResponseType.ERROR
                }
            }
            try {
                dbManager.removeVehicle(id.toLong())
            } catch (e: SQLException) {
                logger.error("Error while removing elements from vehicles $e")
                return Response("Возникла ошибка при удалении объекта из базы данных")
            }
            logger.info("Element was successfully removed from database")
            collectionManager.removeById(id)
            logger.info("Element was removed from collection")
            Response("Элемент успешно удален").apply { responseType = ResponseType.SUCCESS }
        } else {
            logger.error("Invalid index was provided")
            Response("Указан некорректный индекс").apply {
                responseType = ResponseType.ERROR
            }
        }
    }
}