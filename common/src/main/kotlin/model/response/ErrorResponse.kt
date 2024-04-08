package model.response

class ErrorResponse(message: String, private val isFatal: Boolean) : Response(message) {
    init {
        TODO("добавить logger.error")
    }

    fun isFatal(): Boolean {
        return isFatal
    }
}