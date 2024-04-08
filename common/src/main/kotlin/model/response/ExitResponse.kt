package model.response

class ExitResponse(message: String, private val status: Int) : Response(message) {
    init {
        TODO("добавить в logger.info")
    }

    fun getExitCode(): Int {
        return status
    }
}