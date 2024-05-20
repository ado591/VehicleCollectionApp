import model.User
import model.request.Request
import model.request.RequestType
import model.response.Response
import model.response.ResponseType
import network.UDPClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.*
import java.io.IOException
import java.util.Scanner
import kotlin.system.exitProcess


class Client : KoinComponent {
    private val scanner: Scanner = Scanner(System.`in`)
    private val client: UDPClient by inject()
    private var serverResponseRequired: Boolean = false
    private var currentUser: User? = null
    fun interactiveMode(): Nothing {
        do {
            run outer@{
                if (!serverResponseRequired) {
                    val inputLine: String = scanner.nextLine() ?: return@outer
                    val commandLine = inputLine.split("\\s".toRegex())[0]
                    if (commandLine == "execute_script") {
                        val commandArguments = inputLine.split(" ", limit = 2)
                            .takeIf { it.size > 1 }
                            ?.drop(1)
                            ?.joinToString(" ").takeUnless { it.isNullOrBlank() }
                        ExecuteScript(this).execute(commandArguments)
                        return@outer
                    }
                    if (commandLine == "history") {
                        val commandArguments = inputLine.split(" ", limit = 2)
                            .takeIf { it.size > 1 }
                            ?.drop(1)
                            ?.joinToString(" ").takeUnless { it.isNullOrBlank() }
                        println(History.execute(commandArguments))
                        return@outer
                    }
                    var clientRequest = Request(
                        message = inputLine,
                        user = currentUser
                    )
                    // если log_in, sign_up, то обновить currentUser
                    if (commandLine == "log_in" || commandLine == "sign_up") {
                        currentUser?.run {
                            print("Вы уже вошли в систему. Выйти никак")
                            return@outer
                        }
                        val userFromInput = UserBuilder.getUser()
                        clientRequest.user = userFromInput
                    }
                    client.sendData(
                        ObjectMapperWrapper.clientMapper.writeValueAsBytes(
                            clientRequest
                        )
                    )
                    History.addToHistory(inputLine)
                }
                handleResponse()
            }

        } while (true)
    }

    private fun userInputMode() {
        val newElement = ItemBuilder.consoleAdd()
        client.sendData(ObjectMapperWrapper.clientMapper.writeValueAsBytes(Request().apply {
            vehicle = newElement
            requestType = RequestType.DATA
            user = currentUser
        }))
    }

    fun handleResponse() {
        try {
            val serverResponse =
                ObjectMapperWrapper.clientMapper.readValue(client.receiveData(), Response::class.java)
            when (serverResponse.responseType) {
                ResponseType.EXIT -> {
                    println(serverResponse.message)
                    client.disconnectFromServer()
                    scanner.close()
                    exitProcess(serverResponse.statusCode)
                }

                ResponseType.USER_INPUT -> {
                    userInputMode()
                    serverResponseRequired = true
                }

                ResponseType.AUTHORIZATION -> {
                    println(serverResponse.message)
                    currentUser = serverResponse.responseUser
                }

                else -> {
                    println(serverResponse.message)
                    serverResponseRequired = false
                }
            }
        } catch (e: IOException) {
            println("Сервер временно недоступен. Вы сможете повторно отправить запрос через 10 секунд")
            Thread.sleep(10000)
            client.reconnect()
        }
    }

    /**
     * Возвращает текущего пользователя
     */
    fun getCurrentUser(): User? {
        return currentUser
    }
}