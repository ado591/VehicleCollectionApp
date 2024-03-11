package commands

import response.Response
import kotlin.system.exitProcess

class Exit: Command("exit", "завершить программу") {

    override fun execute(argument: String): Response {
        console.print("Программа завершена с кодом 0")
        exitProcess(0) //todo: something else???
    }
}