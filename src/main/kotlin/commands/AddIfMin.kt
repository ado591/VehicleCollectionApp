package commands

import commands.extra.ItemBuilder
import data.Vehicle
import response.Response

class AddIfMin(): Command("add_if_min",
    "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции") {

    /**
     * Adds element to collection using ItemBuilder class and .consoleAdd() method
     * iff new element will be the smallest in the collection
     * @param argument (should be null)
     * @return a Response object with a success message after adding an element or error message if element is not a min
     */
    override fun execute(argument: String?): Response {
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