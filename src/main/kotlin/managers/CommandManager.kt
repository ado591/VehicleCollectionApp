package managers

import commands.Command
import org.koin.core.component.KoinComponent
import kotlin.reflect.full.allSuperclasses
import kotlin.reflect.full.createInstance
import kotlin.reflect.full.isSubclassOf

class CommandManager(): KoinComponent {
    private val allCommands = Command::class.sealedSubclasses
        .map { it.createInstance() as Command }

    fun getCommandList(): List<Command> {
        return allCommands;
    }
}