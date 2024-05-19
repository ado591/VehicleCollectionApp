package commands

import model.User
import model.response.Response

import model.response.ResponseType
import java.util.ResourceBundle
class Help : Command(
    "help",
    ResourceBundle.getBundle("message/info").getString("help_description")
) {

    /**
     * @param argument a string argument (should be null)
     * @return a Response object with the list of available commands and their descriptions
     */
    override fun execute(argument: String?, user: User): Response {
        val result: StringBuilder = StringBuilder()
        for (command in commandManager.getUserCommandList()) {
            result.appendLine("${command.name()}: ${command.description()}")
        }
        return Response(result.toString()).apply { responseType = ResponseType.SUCCESS }
    }
}