package commands

import response.Response
import kotlin.system.exitProcess

class Exit: Command("exit", "завершить программу") {

    override fun execute(args: Array<String>): Response {
        exitProcess(0) //todo: something else???
    }
}