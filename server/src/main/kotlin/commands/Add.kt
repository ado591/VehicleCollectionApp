package commands


import commands.extra.Autogeneratable
import exceptions.InvalidArgumentException
import model.response.Response
import model.response.SuccessResponse
import java.util.ResourceBundle

class Add : Command(
    "add",
    ResourceBundle.getBundle("message/info").getString("add_description")
), Autogeneratable {

    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * @param argument  null for console input, --auto for calling built-in method
     * @return a Response object with a success message after adding an element
     */
    override fun execute(argument: String?): Response {
        val newElement = argument?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException(ResourceBundle.getBundle("message/error").getString("incorrect_flag"))
            autoAdd()
        } ?: builder.consoleAdd()
        collectionManager.add(newElement)
        return SuccessResponse("Элемент успешно добавлен")
    }
}