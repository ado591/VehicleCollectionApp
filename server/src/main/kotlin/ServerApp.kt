import di.serverModule
import managers.CollectionManager
import managers.DatabaseManager
import network.UDPServer
import org.apache.logging.log4j.LogManager
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import java.net.BindException
import java.net.UnknownHostException

fun main() {
    startKoin {
        modules(serverModule)
    }
    Class.forName("org.postgresql.Driver")
    val dbManager: DatabaseManager = object : KoinComponent {
        val manager: DatabaseManager by inject()
    }.manager
    val collectionManager = object : KoinComponent {
        val manager: CollectionManager by inject()
    }.manager
    val logger = LogManager.getLogger("logger")
    try {
        val server = UDPServer()
        server.runServer()
    } catch (e: UnknownHostException) {
        logger.fatal("Could not determinate host or IP address")
    } catch (e: BindException) {
        logger.fatal("This IP and host is already used by another app")
    }
}