package commands

import model.response.Response
import model.response.ResponseType
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
        logger.info("Closing client app")
        return Response("Программа завершена с кодом 0").apply {
            responseType = ResponseType.EXIT
            statusCode = 0
        }
    }
}