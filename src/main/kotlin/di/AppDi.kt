package di

import managers.CollectionManager
import managers.CommandManager
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module

val consoleAppModule = module {
    singleOf(::CommandManager)
    singleOf(::CollectionManager)
}

