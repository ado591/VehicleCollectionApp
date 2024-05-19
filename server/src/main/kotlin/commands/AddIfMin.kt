package commands

import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import model.User
import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class AddIfMin : Command(
    "add_if_min",
    ResourceBundle.getBundle("message/info").getString("addIfMin_description")
), Autogeneratable {

    /**
     * Adds element to collection using auto generation or user input for client
     * iff new element will be the smallest in the collection
     * @param argument  null for user input, --auto for calling built-in method
     * @return a Response object with result of execution
     */
    override fun execute(argument: String?, user: User): Response {
        val newElement = argument?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException("Передан неверный флаг")
            autoAdd()
        } ?: run {
            return Response().apply {
                responseType = ResponseType.USER_INPUT
                index = collectionManager.getSize()
            }
        }
        val customResponse: StringBuilder = StringBuilder().appendLine(newElement.toString())
        if (collectionManager.isEmpty()) {
            logger.warn("Collection is empty")
            return Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        }
        return if (newElement < collectionManager.getMin()) {
            dbManager.addVehicle(newElement, user)
            collectionManager.add(newElement)
            logger.info("Element was added to collection")
            customResponse.appendLine("Элемент успешно добавлен в коллекцию")
            Response(customResponse.toString()).apply { responseType = ResponseType.SUCCESS }
        } else {
            Vehicle.setCurrentId(collectionManager.getSize().toLong())
            logger.warn("Element is not a minimum for collection")
            customResponse.appendLine("Этот элемент не является минимумом для коллекции")
            Response(customResponse.toString()).apply { responseType = ResponseType.WARNING }
        }
    }

    override fun executeWithObject(vehicle: Vehicle, index: Int, user: User): Response {
        vehicle.id = index.toLong()
        val customResponse: StringBuilder = StringBuilder().appendLine(vehicle.toString())
        if (collectionManager.isEmpty()) {
            logger.warn("Collection is empty")
            return Response("Коллекция пуста").apply { responseType = ResponseType.WARNING }
        }
        return if (vehicle < collectionManager.getMin()) {
            dbManager.addVehicle(vehicle, user)
            collectionManager.add(vehicle)
            logger.info("Element was added to collection")
            customResponse.appendLine("Элемент успешно добавлен в коллекцию")
            Response(customResponse.toString()).apply { responseType = ResponseType.SUCCESS }
        } else {
            Vehicle.setCurrentId(collectionManager.getSize().toLong())
            logger.warn("Element is not a minimum for collection")
            customResponse.appendLine("Этот элемент не является минимумом для коллекции")
            Response(customResponse.toString()).apply { responseType = ResponseType.WARNING }
        }
    }
}