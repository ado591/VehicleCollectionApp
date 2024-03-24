package commands

import commands.extra.Autogeneratable
import data.Vehicle
import response.Response
import java.util.*

class Add(): Command("add",
    ResourceBundle.getBundle("message/info").getString("add_description")), Autogeneratable {

    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * @param argument (should be null)
     * @return a Response object with a success message after adding an element
     */
    override fun execute(argument: String?): Response {
        val newElement: Vehicle = when (argument) {
            "--auto" -> builder.autoAdd()
            null -> builder.consoleAdd()
            else -> {
                return Response("Неизвестный флаг команды ${this.name()}")
            }
        }
        collectionManager.add(newElement)
        return Response("Элемент успешно добавлен")
    }
}