package utils

import commands.Command
import managers.CommandManager

class CommandParser {
    companion object {
        private val commandManager: CommandManager by inject()
        private val consoleCommands = commandManager.getCommandMap()
        fun parseCommand(line: List<String>): Command? {
            val commandToProcess: String = line[0]
            return consoleCommands[commandToProcess]
        }
    }
}