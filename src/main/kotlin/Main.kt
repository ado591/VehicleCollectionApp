import commands.Add
import di.consoleAppModule
import org.koin.core.context.GlobalContext.startKoin

fun main(args: Array<String>) {
    startKoin{
        modules(consoleAppModule)
    }
    println("This is a sample Koin app")
    Add().execute(null)
    //todo: should I use System.getenv() or environmentProperties() from Koin? Ask Alexander
}