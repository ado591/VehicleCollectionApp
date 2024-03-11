package commands

import commands.extra.ItemBuilder
import data.Vehicle
import response.Response

class Add(): Command("add", "добавить новый элемент в коллекцию"){

    override fun execute(argument: String): Response {
        val builder = ItemBuilder()
        val newElement: Vehicle = builder.consoleAdd()
        collectionManager.add(newElement)
        return Response("элемент успешно добавлен")
    }
    //todo: add auto generated element for execute_script command
}