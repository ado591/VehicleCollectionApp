package console


import managers.CollectionManager
import managers.CommandManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import java.io.BufferedInputStream
import java.io.FileOutputStream


class Console(inputStream: BufferedInputStream, outputStream: FileOutputStream): KoinComponent {
    private val collectionManager: CollectionManager by inject()
    private val commandManager: CommandManager by inject()
    private val consoleCommands = commandManager.getCommandList().associateBy { it.name() }

    public fun print(message: String) { //todo: разобраться с выводом
        println(message)
    }
}