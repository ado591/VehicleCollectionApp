package managers

import commands.Command
import org.koin.core.component.KoinComponent
import kotlin.reflect.full.createInstance

class CommandManager : KoinComponent {
    private val commandHistory = ArrayDeque<Command>()
    private val allCommands = Command::class.sealedSubclasses
        .map { it.createInstance() }

    fun getCommandList(): List<Command> {
        return allCommands
    }

    fun getCommandMap(): Map<String, Command> {
        return allCommands.associateBy { it.name() }
    }

    fun addToHistory(command: Command) {
        commandHistory.add(command)
    }

    fun getLastCommands(amount: Int): List<Command> {
        return commandHistory.takeLast(amount)
    }

}