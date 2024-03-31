package console

import commands.Command
import exceptions.UnknownCommandException
import managers.CommandManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.util.Scanner
import kotlin.system.exitProcess

/**
 * Closes the Scanner object and exits the program with the specified status.
 *
 * @param status the exit status code
 */
fun Scanner.closeWithCleanup(status: Int): Nothing {
    this.close()
    exitProcess(status)
}

class Console : KoinComponent {
    private val commandManager: CommandManager by inject()
    private val consoleCommands = commandManager.getCommandMap()
    private val scanner: Scanner = Scanner(System.`in`)

    fun print(response: Response) {
        println(response.message())
    }

    fun interactiveMode(): Nothing {
        do {
            val inputLine = scanner.nextLine().split(" ", limit = 2)
            val commandToProcess: Command
            try {
                commandToProcess = parseCommand(inputLine) ?: throw UnknownCommandException() //todo: cringe
            } catch (e: UnknownCommandException) {
                print(Response("Неизвестная команда"))
                continue
            }
            try {
                print(commandToProcess.execute(inputLine.getOrNull(1)))
            } catch (e: IllegalArgumentException) {
                print(Response(e.toString()))
            } finally {
                commandManager.addToHistory(commandToProcess)
            }
        } while (true)
    }

    fun parseCommand(line: List<String>): Command? {
        val commandToProcess: String = line[0]
        return consoleCommands[commandToProcess]
    }

    fun getScanner(): Scanner {
        return scanner
    }

}