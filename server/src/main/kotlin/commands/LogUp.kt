package commands

import exceptions.UserAlreadyExistsException
import model.User
import model.response.Response

class LogUp : Command("log up", "регистрирует пользователя в инновационной программе") {
    override fun execute(argument: String?, user: User): Response {
        return try {
            dbManager.registerUser(user)
            Response(
                """"Вы зарегистрировались в системе. Можете начать дОбАвЛЯтЬ k0рАбЛи и удАлЯтЬ вЕрТ0лЕtЫ
                    ---------------+---------------
              ___ /^^[___              _
             /|^+----+   |#___________//
           ( -+ |____|    ______-----+/
            ==_________--'            \
              ~_|___|__
                    """
            )
        } catch (e: UserAlreadyExistsException) {
            Response("Пользователь с таким именем пользователя уже существует")
        }
    }
}