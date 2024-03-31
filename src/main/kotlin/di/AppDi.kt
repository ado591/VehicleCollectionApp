package di

import commands.extra.ItemBuilder
import managers.CollectionManager
import managers.CommandManager
import console.Console
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val consoleAppModule = module {
    singleOf(::CommandManager)
    singleOf(::Console)
    single(createdAtStart = true) {
        val filePath = System.getenv("FILE_PATH") ?: "src/main/files/loadfile.xml"
        CollectionManager(filePath)
    }
    singleOf(::ItemBuilder)
}

