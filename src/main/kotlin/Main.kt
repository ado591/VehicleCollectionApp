import console.Console
import di.consoleAppModule
import org.koin.core.context.GlobalContext.startKoin

fun main() {
    startKoin {
        modules(consoleAppModule)
    }
    val console = Console()
    console.interactiveMode()
}