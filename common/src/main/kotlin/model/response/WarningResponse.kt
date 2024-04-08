package model.response

class WarningResponse(message: String) : Response(message) {
    //todo: мб наследование от SuccessResponse?
    init {
        TODO("Добавить в logger.warn")
    }
}