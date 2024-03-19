package commands

import response.Response

class Help(): Command("help", "вывести справку по доступным командам"){

    /**
     * @param argument a string argument (should be null)
     * @return a Response object with the list of available commands and their descriptions
     */
    override fun execute(argument: String?): Response {
        val result: StringBuilder = StringBuilder()
        for (command in commandManager.getCommandList()) {
           result.appendLine("${command.name()}: ${command.description()}")
        }
        return Response(result.toString())
    }


}