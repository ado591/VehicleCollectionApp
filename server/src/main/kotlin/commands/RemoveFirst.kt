package commands

import exceptions.users.UserNotAuthorizedException
import model.User
import model.response.Response
import model.response.ResponseType
import java.sql.SQLException
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
    override fun execute(argument: String?, user: User?): Response {
        return if (collectionManager.isEmpty()) {
            logger.warn("Trying to process command with an empty collection")
            Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        } else {
            if (!dbManager.checkCreator(1, user ?: throw UserNotAuthorizedException())) {
                Response("У вас нет прав для модификации данного объекта").apply {
                    responseType = ResponseType.ERROR
                }
            } else {
                try {
                    dbManager.removeVehicle(1)
                } catch (e: SQLException) {
                    return Response("Возникла ошибка при удалении элемента из базы данных. Скорее всего, чет с индексами")
                }
                collectionManager.removeById(0)
                logger.info("First element was removed from the collection")
                Response("Элемент успешно удален").apply { responseType = ResponseType.SUCCESS }
            }
        }
    }
}