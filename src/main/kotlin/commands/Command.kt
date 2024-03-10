package commands

import console.Console
import managers.CollectionManager
import managers.CommandManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response


abstract class Command(val name: String, val description: String): KoinComponent {
    val collectionManager: CollectionManager by inject()
    val commandManager: CommandManager by inject()
    val console: Console by inject()

    fun name(): String = this.name //todo: make easier for localization
    fun description(): String = this.description //todo: make easier for localization
    //todo: тут пока всё non-null, но надо думать
    abstract fun execute(args: Array<String>): Response //todo: some mystery with response messages
}