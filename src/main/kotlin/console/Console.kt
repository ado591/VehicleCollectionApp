package console

import commands.*
import managers.CollectionManager
import managers.CommandManager

class Console(private val collectionManager: CollectionManager, private val commandManager: CommandManager) {
    private val consoleCommands = hashMapOf( // todo: make easier
        "add" to Add(),
        "add_if_min" to AddIfMin(),
        "clear" to Clear(),
        "execute_script" to ExecuteScript(),
        "exit" to Exit(),
        "head" to Head(),
        "help" to Help(),
        "info" to Info(),
        "print_asc" to PrintAsc(),
        "remove_by_id" to RemoveById(),
        "remove_by_type" to RemoveByType(),
        "remove_first" to RemoveFirst(),
        "save" to Save(),
        "show" to Show(),
        "sum_of_fuel" to SumOfFuel(),
        "update_id" to UpdateId()
    )
    // todo: pretty printCollection() ??

    public fun print(message: String) { //todo: разобраться с выводом
        println(message)
    }
}