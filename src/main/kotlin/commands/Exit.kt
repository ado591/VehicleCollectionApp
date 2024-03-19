package commands

import response.Response
import kotlin.system.exitProcess

class Exit: Command("exit", "завершить программу") {

    /**
     * Prints a response message indicating that the program has ended with exit code 0.
     * The method then exits the process with exit code 0
     * @param argument a string argument (should be null)
     * @return a Response object with a message indicating that the program has ended with exit code 0
     */
    override fun execute(argument: String?): Response {
        console.print(Response("Программа завершена с кодом 0")) //todo: всё-таки нужны ли response?
        exitProcess(0)
    }
}