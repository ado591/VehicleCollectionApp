package managers

import commands.Command
import org.koin.core.component.KoinComponent
import kotlin.reflect.full.createInstance

class CommandManager : KoinComponent {
    private val allCommands = Command::class.sealedSubclasses
        .map { it.createInstance() }
    private val userCommands = allCommands.filter { command ->
        !command::class.annotations.any { it.annotationClass.simpleName == "ServerOnly" }
    }

    fun getCommandList(): List<Command> {
        return allCommands
    }

    fun getCommandMap(): Map<String, Command>{
        return allCommands.associateBy { it.name() }
    }

    fun getUserCommandList(): List<Command> {
        return userCommands
    }

    fun getUserCommandMap(): Map<String, Command> {
        return userCommands.associateBy { it.name() }
    }

}