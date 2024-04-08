import di.clientModule
import org.koin.core.context.startKoin

class App {
    fun main() {
        startKoin() {
            modules(clientModule)
        }
        val client = Client()
        client.interactiveMode()
    }
}