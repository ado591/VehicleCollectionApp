package commands

import data.Vehicle
import response.Response

class PrintAsc(): Command("print_ascending","вывести элементы коллекции в порядке возрастания") {

    override fun execute(argument: String): Response {
        val message = StringBuilder()
        val arrayCopy: ArrayDeque<Vehicle>  = collectionManager.getCollection()
        arrayCopy.sort() // todo: implement without extra array?
        for (element in arrayCopy) {
            message.append(element.toString()).append("\n")
        }
        return Response(message.toString())
    }
}