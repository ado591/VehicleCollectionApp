import model.request.Request
import model.request.RequestType
import model.response.Response
import model.response.ResponseType
import network.UDPClient
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import utils.ExecuteScript
import utils.History
import utils.ItemBuilder
import utils.ObjectMapperWrapper
import java.io.IOException
import java.util.Scanner
import kotlin.system.exitProcess


class Client : KoinComponent {
    private val scanner: Scanner = Scanner(System.`in`)
    private val client: UDPClient by inject()
    private var serverResponseRequired: Boolean = false
    fun interactiveMode(): Nothing {
        do {
            run {
                if (!serverResponseRequired) {
                    val inputLine: String = scanner.nextLine() ?: return@run
                    if (inputLine.split("\\s".toRegex())[0] == "execute_script") {
                        val commandArguments = inputLine.split(" ", limit = 2)
                            .takeIf { it.size > 1 }
                            ?.drop(1)
                            ?.joinToString(" ").takeUnless { it.isNullOrBlank() }
                        ExecuteScript(this).execute(commandArguments)
                        return@run
                    }
                    if (inputLine.split("\\s".toRegex())[0] == "history") {
                        val commandArguments = inputLine.split(" ", limit = 2)
                            .takeIf { it.size > 1 }
                            ?.drop(1)
                            ?.joinToString(" ").takeUnless { it.isNullOrBlank() }
                        println(History.execute(commandArguments))
                        return@run
                    }
                    client.sendData(ObjectMapperWrapper.clientMapper.writeValueAsBytes(Request(inputLine)))
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
}