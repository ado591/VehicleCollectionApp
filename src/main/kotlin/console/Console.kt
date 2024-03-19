package console


import data.Vehicle
import managers.CollectionManager
import managers.CommandManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response
import java.io.BufferedInputStream
import java.io.FileOutputStream
import java.util.*


class Console(inputStream: BufferedInputStream, outputStream: FileOutputStream): KoinComponent {
    private val collectionManager: CollectionManager by inject()
    private val commandManager: CommandManager by inject()
    private val consoleCommands = commandManager.getCommandList().associateBy { it.name() }

    private val scanner: Scanner = Scanner(System.in) //todo: todo

    public fun print(response: Response) { //todo: разобраться с выводом
        println(response.message())
    }

    public fun interactiveMode(): Unit {
        do {
            val inputLine = scanner.nextLine().split(" ") //todo: todo
            val commandToProcess: String = inputLine[0]
        } while(true)
    }

}