package network

import org.koin.core.component.KoinComponent
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel


const val DEFAULT_HOST = "localhost"
const val DEFAULT_SERVER_PORT = 8083
const val INTERVAL = 1000L

class UDPClient(ip: String, port: Int) : KoinComponent {
    constructor() : this(DEFAULT_HOST, DEFAULT_SERVER_PORT)

    private val serverAddress: InetSocketAddress = InetSocketAddress(ip, port)
    private var dc: DatagramChannel =
        DatagramChannel.open().bind(null)
            .connect(serverAddress)

    init {
        dc.configureBlocking(false)
    }

    fun disconnectFromServer() {
        dc.disconnect()
    }

    fun sendData(data: ByteArray) {
        val buf: ByteBuffer = ByteBuffer.wrap(data)
        try {
            dc.send(buf, serverAddress)
        } catch (e: IOException) {
            println("Произошла проблема при отправке данных. Попробуйте еще раз")
            Thread.sleep(INTERVAL * 10)
        }
    }


    fun receiveData(): ByteArray {
        val buf: ByteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
        do {
            run outer@{
                dc.receive(buf)?.run {
                    return buf.array()
                } ?: run {
                    Thread.sleep(INTERVAL)
                    return@outer
                }
            }
        } while (true)
    }

    private fun connect() {
        dc =
            DatagramChannel.open().bind(null)
                .connect(serverAddress)
        dc.configureBlocking(false)
    }

    fun reconnect() {
        dc.disconnect()
        dc.close()
        connect()
    }

}