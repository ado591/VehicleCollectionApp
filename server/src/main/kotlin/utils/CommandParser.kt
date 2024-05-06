package utils

import commands.Command
import org.koin.core.component.inject
import exceptions.UnknownCommandException
import managers.CommandManager
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.core.component.KoinComponent

class CommandParser : KoinComponent {
    private val commandManager: CommandManager by inject()
    private val consoleCommands = commandManager.getUserCommandMap()
    private val logger: Logger = LogManager.getLogger("logger")
    fun parseCommand(line: List<String>): Command {
        val commandToProcess = line[0]
        logger.info("Trying to find $commandToProcess in command manager")
        return consoleCommands[commandToProcess] ?: throw UnknownCommandException()
    }

    fun parseArguments(inputLine: String): String? {
        return inputLine.split(" ", limit = 2)
            .takeIf { it.size > 1 }
            ?.drop(1)
            ?.joinToString(" ").takeUnless { it.isNullOrBlank() }
    }

}