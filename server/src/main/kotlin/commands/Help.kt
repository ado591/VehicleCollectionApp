package commands

import model.response.Response
import model.response.SuccessResponse
import java.util.ResourceBundle

class Help : Command(
    "help",
    ResourceBundle.getBundle("message/info").getString("help_description")
) {

    /**
     * @param argument a string argument (should be null)
     * @return a Response object with the list of available commands and their descriptions
     */
    override fun execute(argument: String?): Response {
        val result: StringBuilder = StringBuilder()
        for (command in commandManager.getCommandList()) {
            result.appendLine("${command.name()}: ${command.description()}")
        }
        return SuccessResponse(result.toString())
    }
}