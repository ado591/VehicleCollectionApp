package commands

import response.Response

class ExecuteScript: Command("execute_script",
    "считать и исполнить скрипт из указанного файла. " +
        "В скрипте содержатся команды в таком же виде, в котором их вводит пользователь в интерактивном режиме.") {
    override fun execute(argument: String): Response {
        TODO("Not yet implemented") //todo: recursive script
    }
}