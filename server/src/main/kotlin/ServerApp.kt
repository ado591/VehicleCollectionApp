import di.serverModule
import managers.CollectionManager
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
    val collectionManager = object : KoinComponent {
        val manager: CollectionManager by inject()
    }.manager
    val logger = LogManager.getLogger("logger")
    Runtime.getRuntime().addShutdownHook(Thread(collectionManager::save))
    try {
        val server = UDPServer()
        server.runServer()
    } catch (e: UnknownHostException) {
        logger.fatal("Could not determinate host or IP address")
    } catch (e: BindException) {
        logger.fatal("This IP and host is already used by another app")
    }
}