import di.consoleAppModule
import org.koin.core.context.GlobalContext.startKoin

fun main(args: Array<String>) {
    startKoin{
        modules(consoleAppModule)
    }
    println("This is a sample Koin app")
    //todo: I've just removed everything from here to make Koin great again
    //todo: should I use System.getenv() or environmentProperties() from Koin? Ask Alexander
}