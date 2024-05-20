package utils

import model.User
import java.util.*

class UserBuilder {
    companion object {
        fun getUser(): User {
            return User(username = setUsername(), password = setPassword())
        }

        private fun setUsername(): String {
            println("Введите имя пользователя: ")
            do {
                val name: String? = readlnOrNull()
                if (name.isNullOrBlank()) {
                    println(ResourceBundle.getBundle("messages/error").getString("empty_line"))
                } else if (name.length < 3 || name.length > 50) {
                    println("Имя пользователя должно содержать от 3 до 50 символов")
                } else {
                    return name
                }
            } while (true)
        }

        private fun setPassword(): String {
            println("Введите пароль: ")
            do {
                val password: String? = readlnOrNull()
                if (password.isNullOrBlank()) {
                    println(ResourceBundle.getBundle("messages/error").getString("empty_line"))
                } else if (password.length < 8 || password.length > 24) {
                    println("Пароль должен быть от 8 до 24 символов") //todo: чекнуть сколько там реально символов
                } else {
                    return password
                }
            } while (true)
        }
    }
}