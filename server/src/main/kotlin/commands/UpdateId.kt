package commands

import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import exceptions.users.UserNotAuthorizedException
import model.User
import model.response.Response
import model.response.ResponseType
import java.sql.SQLException
import java.util.ResourceBundle

class UpdateId : Command(
    "update_id",
    ResourceBundle.getBundle("message/info").getString("updateId_description")
), Autogeneratable {
    /**
     * Updates element with given ID in the collection
     * @param argument ID to update
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?, user: User?): Response {
        val args: List<String> =
            argument?.split(" ") ?: throw InvalidArgumentException("Не передан аргумент для команды ${name()}")
        val id: Int = (args[0].let {
            it.toIntOrNull() ?: throw InvalidArgumentException("Аргумент команды должен быть числом")
        })
        val flag: String? = args.getOrNull(1)
        if (collectionManager.isEmpty()) {
            return Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        } else if (!(collectionManager.isExists(id))) {
            throw InvalidArgumentException("Указан некорректный индекс")
        }
        if (!dbManager.checkCreator(id.toLong(), user ?: throw UserNotAuthorizedException())) {
            return Response("У вас нет прав для модификации данного объекта").apply {
                responseType = ResponseType.ERROR
            }
        }
        val newElement: Vehicle = flag?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException("Передан неверный флаг")
            autoAdd()
        } ?: run {
            return Response().apply {
                responseType = ResponseType.USER_INPUT
                index = id
            }
        }
        newElement.id = id.toLong()
        collectionManager.update(id, newElement)
        return Response("Элемент успешно обновлен").apply { responseType = ResponseType.SUCCESS }
    }

    override fun executeWithObject(vehicle: Vehicle, index: Int, user: User): Response {
        if (collectionManager.isEmpty()) {
            return Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        } else if (!(collectionManager.inBounds(index))) {
            throw InvalidArgumentException("Указан некорректный индекс")
        }
        try {
            logger.info("Updating in database with index $index")
            dbManager.updateVehicle(vehicle, index.toLong(), user)
        } catch (e: SQLException) {
            return Response("произошла ошибка при обновлении элемента коллекции в базе данных")
        }
        vehicle.id = index.toLong()
        collectionManager.update(index, vehicle)
        return Response("Элемент успешно обновлен").apply { responseType = ResponseType.SUCCESS }
    }
}