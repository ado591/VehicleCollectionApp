package service

import com.fasterxml.jackson.databind.ObjectMapper
import com.fasterxml.jackson.datatype.jsr310.JavaTimeModule
import model.request.Request
import org.apache.logging.log4j.LogManager
import org.apache.logging.log4j.Logger
import java.net.SocketAddress
import java.util.*
import java.util.function.Function

class ReaderService :
    Function<Pair<ByteArray, SocketAddress>, Pair<Request, SocketAddress>> {
    private val objectMapper =
        ObjectMapper().registerModule(JavaTimeModule()).setTimeZone(TimeZone.getTimeZone("Europe/Moscow"))
    private val logger: Logger = LogManager.getLogger("logger")


    override fun apply(t: Pair<ByteArray, SocketAddress>): Pair<Request, SocketAddress> {
        logger.info("Deserialization from ${t.second}")
        val clientData = t.first
        val clientAddress = t.second
        val clientRequest = objectMapper.readValue(clientData, Request::class.java)
        return Pair(clientRequest, clientAddress)
    }

}