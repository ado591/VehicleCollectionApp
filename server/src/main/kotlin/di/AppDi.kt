package di

import managers.CollectionManager
import managers.CommandManager
import managers.DatabaseManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import utils.CommandParser

val serverModule = module {
    single(createdAtStart = true) {
        DatabaseManager()
    }
    single(createdAtStart = true) {
        CommandManager()
    }
    single(createdAtStart = true) {
        val filePath = System.getenv("FILE_PATH")
            ?: "C:\\Users\\Пользователь\\IdeaProjects\\VehicleConsoleApp\\server\\src\\main\\files\\loadfile.xml"
        CollectionManager(filePath)
    }
    singleOf(::CommandParser)
}

