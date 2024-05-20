package network

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import commands.extra.Autogeneratable
import data.Vehicle
import exceptions.InvalidArgumentException
import exceptions.NoObjectPassedException
import exceptions.UnknownCommandException
import exceptions.users.UserNotAuthorizedException
import exceptions.users.UserNotFoundException
import model.request.Request
import model.response.Response
import model.response.ResponseType
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.core.component.KoinComponent
import utils.RequestHandler
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.util.*


const val DEFAULT_HOST = "localhost"
const val DEFAULT_SERVER_PORT = 8083

class UDPServer(host: String, port: Int) : KoinComponent {
    constructor() : this(DEFAULT_HOST, DEFAULT_SERVER_PORT)

    private val address: InetSocketAddress = InetSocketAddress(host, port)
    private val server: DatagramChannel = DatagramChannel.open().bind(address)
    private val logger: Logger = LogManager.getLogger("logger")
    private val requestHandler = RequestHandler()
    private val objectMapper =
        ObjectMapper().registerModule(JavaTimeModule()).setTimeZone(TimeZone.getTimeZone("Europe/Moscow"))

    init {
        server.configureBlocking(false)
        logger.info("UDP server was created. Host name -- $host, port -- $port")
    }

    private fun sendData(data: ByteArray, clientAddress: SocketAddress) {
        logger.info("Sending data to $clientAddress")
        server.send(ByteBuffer.wrap(data), clientAddress)
    }

    private fun receiveData(): Pair<ByteArray, SocketAddress> {
        val buffer: ByteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
        var remoteAddress: SocketAddress?
        do {
            remoteAddress = server.receive(buffer)
        } while (remoteAddress == null)
        buffer.flip()
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        logger.info("Data was successfully received from $remoteAddress")
        return Pair(data, remoteAddress)
    }

    private fun getObjectFromUser(address: SocketAddress, response: Response): Vehicle {
        logger.info("Listening for client with address $address")
        sendData(objectMapper.writeValueAsBytes(response), address)
        val buf = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
        do {
            run {
                buf.clear()
                val senderAddress: SocketAddress = server.receive(buf) ?: return@run
                if (senderAddress == address) {
                    buf.flip()
                    val receivedData = ByteArray(buf.remaining())
                    buf.get(receivedData)
                    val receivedResponse = objectMapper.readValue(receivedData, Request::class.java)
                    return receivedResponse.vehicle ?: throw NoObjectPassedException()
                }
            }
        } while (true)
    }


    fun runServer(): Nothing {
        logger.info("Starting server...")
        do {
            run {
                val dataFromClient: Pair<ByteArray, SocketAddress>
                try {
                    dataFromClient = receiveData()
                } catch (e: IOException) {
                    logger.error("Error while receiving data from client")
                    return@run
                }
                val clientData = dataFromClient.first
                val clientAddress = dataFromClient.second
                val clientRequest: Request
                try {
                    clientRequest = objectMapper.readValue(clientData, Request::class.java)
                } catch (e: IOException) {
                    val error =
                        Response("Возникла ошибка при получении данных").apply { responseType = ResponseType.ERROR }
                    logger.error("Error occurred while deserialization process")
                    sendData(objectMapper.writeValueAsBytes(error), clientAddress)
                    return@run
                }
                logger.info("Handle request from user with login ${clientRequest.user}")
                val serverResponse: Response = handleAuthorizedRequest(clientRequest, clientAddress)
                /*val serverResponse: Response = clientRequest?.run {
                    handleAuthorizedRequest(clientRequest, clientAddress)
                }
                    ?: Response("Вы не вошли в систему! Используйте команду sign up для регистрации или log in для входа").apply {
                        responseType = ResponseType.WARNING
                    }*/
                sendData(objectMapper.writeValueAsBytes(serverResponse), clientAddress)
            }
        } while (true)
    }

    private fun handleAuthorizedRequest(clientRequest: Request, clientAddress: SocketAddress): Response {
        var serverResponse: Response
        try {
            val result = requestHandler.handle(clientRequest)
            val commandToProcess = result.first
            serverResponse = result.second
            if (serverResponse.responseType == ResponseType.USER_INPUT && commandToProcess is Autogeneratable) {
                val element: Vehicle = getObjectFromUser(clientAddress, serverResponse)
                serverResponse =
                    requestHandler.handleWithObject(
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
}