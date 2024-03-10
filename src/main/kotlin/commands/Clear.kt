package commands

import response.Response
import java.util.ResourceBundle

class Clear(): Command("clear", "очистить коллекцию") {

    override fun execute(args: Array<String>): Response {
        //todo: any troubles with collection??
        collectionManager.clear()
        return Response(ResourceBundle.getBundle("success_message").getString("clear"))
    }
}