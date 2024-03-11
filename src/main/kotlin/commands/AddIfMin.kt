package commands

import commands.extra.ItemBuilder
import data.Vehicle
import response.Response

class AddIfMin(): Command("add_if_min",
    "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции") {

    override fun execute(argument: String): Response {
        val newElement: Vehicle = ItemBuilder().consoleAdd()
        return if (newElement < collectionManager.getMin()) {
            collectionManager.add(newElement);
            Response("Элемент успешно добавлен в коллекцию")
        } else {
            Response("Этот элемент не является минимумом для коллекции")
        }
        //todo: add auto generated element ???????
    }
}