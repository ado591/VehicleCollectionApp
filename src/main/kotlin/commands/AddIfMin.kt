package commands

import commands.extra.Autogeneratable
import data.Vehicle
import response.Response
import java.util.*

class AddIfMin(): Command("add_if_min",
    ResourceBundle.getBundle("message/info").getString("addIfMin_description")), Autogeneratable {

    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * iff new element will be the smallest in the collection
     * @param argument (should be null)
     * @return a Response object with a success message after adding an element or error message if element is not a min
     */
    override fun execute(argument: String?): Response {
        val newElement: Vehicle = when (argument) {
            "--auto" -> builder.autoAdd()
            null -> builder.consoleAdd()
            else -> {
                return Response("Неизвестный флаг команды ${this.name()}")
            }
        }
        if (collectionManager.isEmpty()) {
            return Response("Коллекция пуста")
        }
        return if (newElement < collectionManager.getMin()) {
            collectionManager.add(newElement)
            Response("Элемент успешно добавлен в коллекцию")
        } else {
            Response("Этот элемент не является минимумом для коллекции")
        }
    }
}