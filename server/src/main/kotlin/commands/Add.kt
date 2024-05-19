package commands


import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import model.User
import model.response.Response
import model.response.ResponseType
import java.util.ResourceBundle

class Add : Command(
    "add",
    ResourceBundle.getBundle("message/info").getString("add_description")
), Autogeneratable {


    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * @param argument  null for input from client app, --auto for calling built-in method
     * @return a Response object with a success message after adding an element or a Response object requesting an object
     */
    override fun execute(argument: String?, user: User): Response {
        val newElement = argument?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException(ResourceBundle.getBundle("message/error").getString("incorrect_flag"))
            autoAdd()
        } ?: run {
            return Response().apply { responseType = ResponseType.USER_INPUT
                index = collectionManager.getSize()
            }
        }
        dbManager.addVehicle(newElement, user)
        collectionManager.add(newElement)
        logger.info("Element was added to collection")
        return Response("Элемент успешно добавлен").apply { responseType = ResponseType.SUCCESS }
    }

    /**
     * Adds element to collection
     * @param vehicle an object for adding constructed by client app
     */

    override fun executeWithObject(vehicle: Vehicle, index: Int, user: User): Response {
        dbManager.addVehicle(vehicle, user)

        vehicle.id = index.toLong()
        collectionManager.add(vehicle)
        logger.info("Element was added to collection")
        return Response("Элемент успешно добавлен").apply { responseType = ResponseType.SUCCESS }
    }
}