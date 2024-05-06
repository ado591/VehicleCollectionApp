package utils

import java.lang.StringBuilder

const val DEFAULT_AMOUNT = 10
class History {
    companion object {
        private val commandHistory = ArrayDeque<String>()

        /**
         * prints last commands
         * @param argument amount of commands from history. If input is not integer it will be replaced with DEFAULT_AMOUNT
         */
        fun execute(argument: String?): String {
            val amount = argument?.toIntOrNull() ?: DEFAULT_AMOUNT
            if (amount <= 0) return "Количество команд должно быть натуральным числом"
            if (getLastCommands(amount).isEmpty()) {
                return "История команд пуста"
            }
            val response = StringBuilder()
                .appendLine("Список последних $amount команд:")
            for (input in getLastCommands(amount)) {
                response.appendLine(input)
            }
            return response.toString()
        }

        fun addToHistory(command: String) {
            commandHistory.add(command)
        }

        private fun getLastCommands(amount: Int): List<String> {
            return commandHistory.takeLast(amount)
        }
    }
}