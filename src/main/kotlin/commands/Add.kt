package commands

import data.Vehicle
import response.Response
import java.util.ResourceBundle

class Add(): Command("add", "добавить новый элемент в коллекцию"){

    override fun execute(args: Array<String>): Response {
        val newElement: Vehicle = TODO()
        collectionManager.add(newElement)
        return Response(ResourceBundle.getBundle("success_message").getString("add"))
    }
    //todo: add auto generated element for execute_script command
}