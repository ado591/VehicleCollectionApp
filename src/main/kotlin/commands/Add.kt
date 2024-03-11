package commands

import commands.extra.ItemBuilder
import data.Vehicle
import response.Response
import java.time.ZonedDateTime
import java.util.ResourceBundle

class Add(): Command("add", "добавить новый элемент в коллекцию"){

    override fun execute(args: Array<String>?): Response {
        val builder = ItemBuilder()
        val id = collectionManager.getSize() + 1
        val creationDate: ZonedDateTime = ZonedDateTime.now()
        val newElement: Vehicle = builder.consoleAdd()
        collectionManager.add(newElement)
        return Response("элемент успешно добавлен")
    }
    //todo: add auto generated element for execute_script command
}