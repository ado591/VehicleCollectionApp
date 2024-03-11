package commands

import response.Response

class Help(): Command("help", "вывести справку по доступным командам"){ //todo: how to inject command manager
    override fun execute(argument: String): Response {
        val result: StringBuilder = StringBuilder()
        for (command in commandManager.getCommandList()) {
           result.append("${command.name()}: ${command.description()}\n") // todo: check append without \n
        }
        return Response(result.toString())
    }


}