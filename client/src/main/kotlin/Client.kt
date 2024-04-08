import data.Vehicle
import model.response.ErrorResponse
import model.response.ExitResponse
import model.response.Response
import model.response.UserInputResponse
import org.koin.core.component.inject
import network.UDPClient
import utils.ItemBuilder
import java.util.Scanner
import kotlin.system.exitProcess

fun Scanner.closeWithCleanup(status: Int): Nothing { // todo: надо еще соединения все позакрывать
    this.close()
    exitProcess(status)
}

class Client {
    private val scanner: Scanner = Scanner(System.`in`)
    private val client: UDPClient by inject()
    fun interactiveMode(): Nothing {
        do {
            run {
                val inputLine = scanner.nextLine().split(" ", limit = 2)
                val commandArguments = inputLine
                    .takeIf { it.size > 1 }
                    ?.drop(1)
                    ?.joinToString(" ").takeUnless { it.isNullOrBlank() }

                inputLine.getOrNull(0)?.let { client.sendData("$it $commandArguments") } ?: return@run

                when (val serverResponse: Response = client.receiveData()) { // todo: ответ с сервера
                    is UserInputResponse -> {
                        val element: Vehicle = ItemBuilder.consoleAdd()
                        client.sendData(element) //  todo: как сделать так, чтобы сервер понял, что ждет именно объект Vehicle?
                    }

                    is ExitResponse -> {
                        scanner.closeWithCleanup(serverResponse.getExitCode())
                    }

                    is ErrorResponse -> {
                        if (serverResponse.isFatal()) {
                            scanner.closeWithCleanup(1)
                        }
                    }

                    else -> {
                        println(serverResponse.message())
                    }
                }
            }

        } while (true)

    }
}