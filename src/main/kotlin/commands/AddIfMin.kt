package commands

import data.Vehicle
import response.Response
import java.util.ResourceBundle

class AddIfMin(): Command("add_if_min",
    "добавить новый элемент в коллекцию, если его значение меньше, чем у наименьшего элемента этой коллекции") {

    override fun execute(args: Array<String>?): Response {
        val newElement: Vehicle = TODO("Parsing input")
        return if (newElement < collectionManager.getMin()) {
            collectionManager.add(newElement);
            Response(ResourceBundle.getBundle("success_message").getString("add_min"))
        } else {
            Response(ResourceBundle.getBundle("error_message").getString("not_min_provided"))
        }
        //todo: add auto generated element ???????
    }
}