package console

import commands.Command
import managers.CommandManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.util.*


class Console(): KoinComponent {
    private val commandManager: CommandManager by inject()
    private val consoleCommands = commandManager.getCommandMap()
    private val scanner: Scanner = Scanner(System.`in`)

    fun print(response: Response) {
        println(response.message())
    }


    fun interactiveMode() {
        do {
            val inputLine = scanner.nextLine().split(" ")
            val commandToProcess = parseCommand(inputLine)
            try {
                print(commandToProcess!!.execute(inputLine.getOrNull(1)))
                commandManager.addToHistory(commandToProcess)
            } catch (e: NullPointerException) {
                print(Response("Неизвестная команда"))
            }
        } while(true)
    }

    fun parseCommand(line: List<String>): Command? {
        val commandToProcess: String = line[0]
        return try {
            consoleCommands[commandToProcess]!!
        } catch (e: NullPointerException) {
            null
        }
    }

    fun getScanner(): Scanner {
        return scanner
    }

}