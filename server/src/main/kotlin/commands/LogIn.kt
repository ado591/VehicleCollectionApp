package commands

import commands.extra.Authorization
import exceptions.users.InvalidPasswordException
import exceptions.users.UserNotFoundException
import model.User
import model.response.Response
import model.response.ResponseType

@Authorization
class LogIn : Command("log_in", "войти в систему") {
    override fun execute(argument: String?, user: User?): Response {
        return try {
            dbManager.login(user ?: return Response("Не передан пользователь для входа в систему"))
            Response("Вы вошли в систему. Выйти из нее можно только в окно)))").apply {
                responseType = ResponseType.AUTHORIZATION
                responseUser = user
            }
        } catch (e: InvalidPasswordException) {
            Response("Попытка вход в чужой аккаунт для работы с инновационным приложением по работе с коллекцией. За вами уже выехали.")
        } catch (e: UserNotFoundException) {
            Response("Пользователь с таким именем не найден.")
        }
    }
}