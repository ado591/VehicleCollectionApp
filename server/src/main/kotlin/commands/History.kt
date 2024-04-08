package commands

import model.response.Response
import model.response.SuccessResponse
import model.response.WarningResponse
import java.lang.StringBuilder
import java.util.ResourceBundle

const val DEFAULT_AMOUNT = 10

class History : Command(
    "history",
    ResourceBundle.getBundle("message/info").getString("history_description")
) {
    override fun execute(argument: String?): Response { // todo: обработать неверное количество для истории
        val amount = argument?.toIntOrNull() ?: DEFAULT_AMOUNT
        if (commandManager.getLastCommands(amount).isEmpty()) {
            return WarningResponse("История команд пуста")
        }
        val response = StringBuilder()
            .appendLine("Список последних $amount команд:")
        for (command in commandManager.getLastCommands(amount)) {
            response.appendLine(command.name())
        }
        return SuccessResponse(response.toString())
    }
}