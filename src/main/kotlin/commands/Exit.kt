package commands

import console.closeWithCleanup
import response.Response
import java.util.ResourceBundle

class Exit : Command(
    "exit",
    ResourceBundle.getBundle("message/info").getString("exit_description")
) {

    /**
     * Prints a response message indicating that the program has ended with exit code 0.
     * The method then exits the process with exit code 0
     * @param argument a string argument (should be null)
     * @return a Response object with a message indicating that the program has ended with exit code 0
     */
    override fun execute(argument: String?): Response {
        console.print(Response("Программа завершена с кодом 0"))
        console.getScanner().closeWithCleanup(0)
    }
}