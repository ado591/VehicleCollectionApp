package commands

import commands.extra.Authorization
import exceptions.users.UserAlreadyExistsException
import exceptions.users.UserNotFoundException
import model.User
import model.response.Response
import model.response.ResponseType

@Authorization
class SignUp : Command("sign_up", "регистрирует пользователя в инновационной программе") {
    override fun execute(argument: String?, user: User?): Response {
        return try {
            dbManager.registerUser(user ?: throw UserNotFoundException())
            Response(
                """"Вы зарегистрировались в системе. Можете начать дОбАвЛЯтЬ k0рАбЛи и удАлЯтЬ вЕрТ0лЕtЫ
                    ---------------+---------------
              ___ /^^[___              _
             /|^+----+   |#___________//
           ( -+ |____|    ______-----+/
            ==_________--'            \
              ~_|___|__
                    """
            ).apply {
                responseType = ResponseType.AUTHORIZATION
                responseUser = user
            }
        } catch (e: UserAlreadyExistsException) {
            Response("Пользователь с таким именем пользователя уже существует")
        }
    }
}