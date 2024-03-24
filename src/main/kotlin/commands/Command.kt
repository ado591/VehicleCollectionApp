package commands

import commands.extra.ItemBuilder
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
    val builder: ItemBuilder by inject()
    fun name(): String = this.name
    fun description(): String = this.description
    abstract fun execute(argument: String?): Response
}