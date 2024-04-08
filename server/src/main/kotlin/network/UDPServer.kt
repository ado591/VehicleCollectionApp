package network

import java.net.DatagramPacket
import java.net.InetSocketAddress
import java.net.SocketAddress
import java.nio.ByteBuffer
import java.nio.channels.DatagramChannel


const val DEFAULT_IP = "localhost" //todo: подумать
const val DEFAULT_SERVER_PORT = 8083
class UDPServer(ip: String, port: Int) {
    constructor() : this(DEFAULT_IP, DEFAULT_SERVER_PORT)

    private val address: InetSocketAddress = InetSocketAddress(ip, port)
    private val server: DatagramChannel = DatagramChannel.open().bind(address) //todo: логгирование и try-catch?

    fun connectToClient(address: InetSocketAddress) {

    }

    // todo: вроде готово?~

    fun sendData(data: ByteArray) {
        val packet = DatagramPacket(data, data.size)
        val clientAddress = InetSocketAddress(packet.address, packet.port)
        server.send(ByteBuffer.wrap(data), clientAddress)
    }

    fun receiveData(): ByteArray {
        val buffer: ByteBuffer = ByteBuffer.allocate(DEFAULT_BUFFER_SIZE)
        server.receive(buffer)
        buffer.flip()
        val data = ByteArray(buffer.remaining())
        buffer.get(data)
        return data
    }


}