package commands

import response.Response
import java.util.* // todo: no .* imports

/**
 * Выводит пару id + значение для всех элементов коллекции
 */
class Show(): Command("show", "вывести в стандартный поток вывода все элементы коллекции в строковом представлении") {

    override fun execute(args: Array<String>): Response {
        val message = StringBuilder(ResourceBundle.getBundle("info_messages").getString("collection"))
        for (elem in collectionManager.getCollection()) {
            message.append(elem.toString()).append("\n") // todo: with append \n
        }
        return Response(message.toString())
    }

}