package commands

import managers.CollectionManager
import managers.CommandManager
import managers.DatabaseManager
import model.User
import model.response.Response
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger


sealed class Command(private val name: String, private val description: String) : KoinComponent {
    val collectionManager: CollectionManager by inject()
    val commandManager: CommandManager by inject()
    val dbManager: DatabaseManager by inject()
    val logger: Logger = LogManager.getLogger("logger")
    fun name(): String = this.name
    fun description(): String = this.description
    abstract fun execute(argument: String?, user: User): Response
}