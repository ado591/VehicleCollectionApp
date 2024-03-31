package commands

import commands.extra.Autogeneratable
import exceptions.InvalidArgumentException
import response.Response
import java.util.ResourceBundle

class Add : Command(
    "add",
    ResourceBundle.getBundle("message/info").getString("add_description")
), Autogeneratable {

    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * @param argument -- null for console input, --auto for calling built-in method
     * @return a Response object with a success message after adding an element
     */
    override fun execute(argument: String?): Response {
        val newElement = argument?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException()
            builder.autoAdd()
        } ?: builder.consoleAdd()
        collectionManager.add(newElement)
        return Response("Элемент успешно добавлен")
    }
}