package commands

import exceptions.users.InvalidPasswordException
import exceptions.users.UserNotFoundException
import model.User
import model.response.Response


class LogIn : Command("log in", "войти в систему") {
    override fun execute(argument: String?, user: User): Response {
        argument ?: return Response("У этой команды нет аргументов") //todo: а надо ли оно тут
        return try {
            dbManager.login(user)
            Response("Вы вошли в систему. Выйти из нее можно только в окно)))")
        } catch (e: InvalidPasswordException) {
            Response("Попытка вход в чужой аккаунт для работы с инновационным приложением по работе с коллекцией. За вами уже выехали.")
        } catch (e: UserNotFoundException) {
            Response("Пользователь с таким именем не найден.")
        }
    }
}