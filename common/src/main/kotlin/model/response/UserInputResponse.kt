package model.response

class UserInputResponse(message: String, private val commandName: String) : Response(message) {
    init {
        TODO("добавить в logger.info")
    }

    /**
     * @return name of command which will use user input
     */
    fun getCommandName(): String {
        return commandName
    }
}