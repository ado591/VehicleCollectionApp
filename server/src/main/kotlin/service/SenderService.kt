package service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import model.response.Response
import network.UDPServer
import java.net.SocketAddress
import java.util.*
import java.util.function.*

class SenderService(private val server: UDPServer) : Consumer<Pair<Response, SocketAddress>> {
    private val objectMapper =
        ObjectMapper().registerModule(JavaTimeModule()).setTimeZone(TimeZone.getTimeZone("Europe/Moscow"))

    override fun accept(t: Pair<Response, SocketAddress>) {
        server.sendData(objectMapper.writeValueAsBytes(t.first), t.second)
    }
}