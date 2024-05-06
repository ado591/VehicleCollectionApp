import di.clientModule
import org.koin.core.context.startKoin

fun main() {
    startKoin {
        modules(clientModule)
    }
    val client = Client()
    client.interactiveMode()
}