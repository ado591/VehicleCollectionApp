package commands

import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import response.Response
import java.util.ResourceBundle

class AddIfMin : Command(
    "add_if_min",
    ResourceBundle.getBundle("message/info").getString("addIfMin_description")), Autogeneratable {

    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * iff new element will be the smallest in the collection
     * @param argument - null for console input, auto for calling built-in method
     * @return a Response object with a success message after adding an element or error message if element is not a min
     */
    override fun execute(argument: String?): Response {
        val newElement = argument?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException("Передан неверный флаг")
            builder.autoAdd()
        } ?: builder.consoleAdd()
        console.print(Response(newElement.toString()))
        if (collectionManager.isEmpty()) {
            return Response("Коллекция пуста")
        }
        return if (newElement < collectionManager.getMin()) {
            collectionManager.add(newElement)
            Response("Элемент успешно добавлен в коллекцию")
        } else {
            Vehicle.setCurrentId(collectionManager.getSize().toLong())
            Response("Этот элемент не является минимумом для коллекции")
        }
    }
}