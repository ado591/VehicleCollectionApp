package service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import commands.Command
import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import exceptions.NoObjectPassedException
import exceptions.UnknownCommandException
import exceptions.users.UserNotAuthorizedException
import exceptions.users.UserNotFoundException
import model.User
import model.request.Request
import model.request.RequestType
import model.response.Response
import model.response.ResponseType
import network.UDPServer
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import utils.CommandParser
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.util.*
import java.util.concurrent.*
import java.util.function.Function

class RequestService(private val dc: DatagramChannel, private val server: UDPServer) : KoinComponent,
    Function<Pair<Request, SocketAddress>, Pair<Response, SocketAddress>> {
    private val commandParser: CommandParser by inject()
    private val logger: Logger = LogManager.getLogger("logger")
    private val objectMapper =
        ObjectMapper().registerModule(JavaTimeModule()).setTimeZone(TimeZone.getTimeZone("Europe/Moscow"))


    override fun apply(t: Pair<Request, SocketAddress>): Pair<Response, SocketAddress> {
        val response: Response = handleAuthorizedRequest(t.first, t.second)
        return Pair(response, t.second)
    }

    private fun handleAuthorizedRequest(clientRequest: Request, clientAddress: SocketAddress): Response {
        var serverResponse: Response
        try {
            val result = handle(clientRequest)
            val commandToProcess = result.first
            serverResponse = result.second
            if (serverResponse.responseType == ResponseType.USER_INPUT && commandToProcess is Autogeneratable) {
                val futureElement = CompletableFuture.supplyAsync {
                    getObjectFromUser(clientAddress, serverResponse)
                }
                futureElement.join()
                val element = futureElement.get()
                logger.info("Object from user $element")
                logger.info("Command to process $commandToProcess")
                serverResponse =
                    handleWithObject(
                        element,
                        commandToProcess,
                        serverResponse.index,
                        clientRequest.user
                    )
            }
        } catch (e: UnknownCommandException) {
            logger.error("Could not find command")
            serverResponse = Response("Неизвестная команда").apply {
                responseType = ResponseType.ERROR
            }
        } catch (e: InvalidArgumentException) {
            logger.error("Invalid arguments for command")
            serverResponse = Response(e.message ?: "Переданы неверные аргументы").apply {
                responseType = ResponseType.ERROR
            }
        } catch (e: NoObjectPassedException) {
            logger.error("No valid object for command")
            serverResponse = Response("Для выполнения команды требуется передать объект").apply {
                responseType = ResponseType.ERROR
            }
        } catch (e: UserNotAuthorizedException) {
            serverResponse =
                Response("Вы не вошли в систему! Используйте команду sign_up для регистрации или log_in для входа").apply {
                    responseType = ResponseType.WARNING
                }
        }
        return serverResponse
    }

    private fun handle(request: Request): Pair<Command, Response> {
        logger.info("Trying to handle $request")
        val inputLine = request.message
        val commandToProcess = commandParser.parseCommand(inputLine.split(" ", limit = 2))
        val commandArguments = commandParser.parseArguments(inputLine)
        logger.info("command to process: $commandToProcess")
        val user: User? = request.user
        return if (request.requestType == RequestType.SCRIPT && commandToProcess is Autogeneratable) {
            Pair(commandToProcess, commandToProcess.execute((commandArguments?.let { "$it --auto" } ?: "--auto"), user))
        } else if (request.requestType == RequestType.AUTHORIZATION) {
            Pair(commandToProcess, commandToProcess.execute(commandArguments, user))
        } else {
            request.user ?: throw UserNotAuthorizedException()
            Pair(commandToProcess, commandToProcess.execute(commandArguments, user))
        }
    }

    private fun handleWithObject(
        element: Vehicle,
        commandToProcess: Autogeneratable,
        index: Int,
        user: User?
    ): Response {
        return commandToProcess.executeWithObject(element, index, user ?: throw UserNotFoundException())
    }


    private fun getObjectFromUser(address: SocketAddress, response: Response): Vehicle {
        logger.info("Listening for client with address $address")
        server.sendData(objectMapper.writeValueAsBytes(response), address)
        val buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
        do {
            run {
                buf.clear()
                val senderAddress: SocketAddress = dc.receive(buf) ?: return@run
                if (senderAddress == address) {
                    buf.flip()
                    val receivedData = ByteArray(buf.remaining())
                    buf.get(receivedData)
                    val receivedResponse = objectMapper.readValue(receivedData, Request::class.java)
                    logger.info("I have a response")
                    return receivedResponse.vehicle ?: throw NoObjectPassedException()
                }
            }
        } while (true)
    }

}