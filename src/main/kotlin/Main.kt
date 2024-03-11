import di.consoleAppModule
import managers.CollectionManager
import managers.CommandManager
import org.koin.core.context.GlobalContext.startKoin
import utility.XmlReader
import utility.XmlWriter

fun main(args: Array<String>) {
    startKoin{
        modules(consoleAppModule)
    }
    val filePath = System.getenv("${FILE_PATH}")
    val collectionManager = CollectionManager(filePath)
    val loader = XmlReader()
    val write = XmlWriter()
    val commandManager = CommandManager()


    while (true) {
        try {

        }
    }
}