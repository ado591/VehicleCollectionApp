package di

import managers.CommandManager
import org.koin.dsl.module

val clientModule = module {
    single(createdAtStart = true) {
        CommandManager()
    }
}

