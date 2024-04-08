package network

import org.koin.core.component.KoinComponent
import java.io.IOException
import java.net.InetSocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel

const val DEFAULT_IP = "localhost"
const val DEFAULT_SERVER_PORT = 8083

class UDPClient(ip: String, port: Int) : KoinComponent {
    constructor() : this(DEFAULT_IP, DEFAULT_SERVER_PORT)

    private val serverAddress: InetSocketAddress = InetSocketAddress(ip, port)
    private val dc: DatagramChannel =
        DatagramChannel.open().bind(null) //todo: open выбрасывает IOException
            .connect(serverAddress) // todo: как нормально привязать к локальному адресу через bind? без Null

    init {
        dc.configureBlocking(false)
        // todo: добавить логгирование при успешном подключении
    }

    fun sendData(data: ByteArray) {
        val buf: ByteBuffer = ByteBuffer.wrap(data) //todo: indexOutOfBoundException????
        try {
            dc.send(buf, serverAddress)
            // todo: логгирование при успешной отправке
        } catch (e: IOException) {
            //todo: добавить логгирование при ошибке + мб какую-то обработку??
        }
    }

    fun receiveData(): ByteArray {
        val buf: ByteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE) //todo: взяла из внутренностей kotlin
        dc.receive(buf) // todo: проверка на null
        return buf.array()
    }
}