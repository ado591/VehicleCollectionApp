package commands

import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import response.Response
import java.util.ResourceBundle

class UpdateId : Command(
    "update_id",
    ResourceBundle.getBundle("message/info").getString("updateId_description")), Autogeneratable {
    /**
     * Updates element with given ID in the collection
     * @param argument ID to update
     * @return a Response object with a success message or an error message based on the operation result
     */
    override fun execute(argument: String?): Response {
        val args: List<String> =
            argument?.split(" ") ?: throw InvalidArgumentException("Не передан аргумент для команды ${name()}")
        val id: Int = (args[0].let {
            it.toIntOrNull() ?: throw InvalidArgumentException("Аргумент команды должен быть числом")
        }) - 1
        val flag: String? = args.getOrNull(1)
        if (collectionManager.isEmpty()) {
            return Response("Коллекция пуста")
        } else if (!(collectionManager.inBounds(id))) {
            throw InvalidArgumentException("Указан некорректный индекс")
        }
        val newElement: Vehicle = flag?.let {
            if (!checkFlag(it))
                throw InvalidArgumentException("Передан неверный флаг")
            builder.autoAdd()
        } ?: builder.consoleAdd()
        newElement.id = id.toLong() + 1
        collectionManager.update(id, newElement)
        return Response("Элемент успешно обновлен")
    }
}