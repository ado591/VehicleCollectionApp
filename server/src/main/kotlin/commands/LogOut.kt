package commands

import commands.extra.Authorization
import model.User
import model.response.Response
import model.response.ResponseType

@Authorization
class LogOut : Command("log_out", "выход из аккаунта") {
    override fun execute(argument: String?, user: User?): Response {
        user?.run {
            return Response("Счастливой учебы на ФТМИ").apply {
                responseUser = null
                responseType = ResponseType.AUTHORIZATION
            }
        }
            ?: return Response("А зачем выходить, если ты не зашел?").apply { responseType = ResponseType.WARNING }
    }
}