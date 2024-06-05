package network

import model.request.Request
import model.response.Response
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import org.koin.core.component.KoinComponent
import service.ReaderService
import service.RequestService
import service.SenderService
import java.io.IOException
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel
import java.util.concurrent.*
import java.util.concurrent.locks.*
import java.util.function.*
import java.util.function.Function


const val DEFAULT_HOST = "localhost"
const val DEFAULT_SERVER_PORT = 8083

class UDPServer(host: String, port: Int) : KoinComponent {
    constructor() : this(DEFAULT_HOST, DEFAULT_SERVER_PORT)

    private val address: InetSocketAddress = InetSocketAddress(host, port)
    private val channel: DatagramChannel = DatagramChannel.open().bind(address)
    private val logger: Logger = LogManager.getLogger("logger")
    private val lock = ReentrantLock()

    init {
        channel.configureBlocking(false)
        logger.info("UDP server was created. Host name -- $host, port -- $port")
    }

    fun sendData(data: ByteArray, clientAddress: SocketAddress) {
        /*synchronized(this) {
            logger.info("Sending data to $clientAddress")
            channel.send(ByteBuffer.wrap(data), clientAddress)
        }*/
        lock.lock()
        try {
            logger.info("Sending data to $clientAddress")
            channel.send(ByteBuffer.wrap(data), clientAddress)
        } finally {
            lock.unlock()
        }
    }

    private fun receiveData(): Pair<ByteArray, SocketAddress> {
        val buffer: ByteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
        var remoteAddress: SocketAddress?
        do {
            remoteAddress = channel.receive(buffer)
        } while (remoteAddress == null)
        buffer.flip()
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        logger.info("Data was successfully received from $remoteAddress")
        return Pair(data, remoteAddress)
    }


    fun runServer(): Nothing {
        logger.info("Starting server...")
        val executor = Executors.newFixedThreadPool(10) // жалею свой ноут
        do {
            run {
                val dataFromClient: Pair<ByteArray, SocketAddress>
                try {
                    dataFromClient = receiveData()
                } catch (e: IOException) {
                    logger.error("Error while receiving data from client")
                    return@run
                }
                val requestReader: Function<Pair<ByteArray, SocketAddress>, Pair<Request, SocketAddress>> =
                    ReaderService()
                val requestExecutor: Function<Pair<Request, SocketAddress>, Pair<Response, SocketAddress>> =
                    RequestService(channel, this)
                val responseSender: Consumer<Pair<Response, SocketAddress>> = SenderService(this)
                CompletableFuture
                    .supplyAsync { dataFromClient }
                    .thenApplyAsync(requestReader)
                    .thenApplyAsync(requestExecutor)
                    .thenAcceptAsync({ p: Pair<Response, SocketAddress> ->
                        executor.execute {
                            responseSender.accept(p)
                        }
                    }, Executors.newSingleThreadExecutor())
            }
        } while (true)
    }
}