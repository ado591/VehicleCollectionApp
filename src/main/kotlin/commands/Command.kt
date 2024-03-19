package commands

import console.Console
import managers.CollectionManager
import managers.CommandManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import response.Response


sealed class Command(private val name: String, private val description: String): KoinComponent {
    val collectionManager: CollectionManager by inject()
    val commandManager: CommandManager by inject()
    val console: Console by inject()

    fun name(): String = this.name //todo: make easier for localization
    fun description(): String = this.description //todo: make easier for localization
    abstract fun execute(argument: String?): Response //todo: some mystery with response messages
}