package di

import network.UDPClient
import org.koin.dsl.module

val clientModule = module {
    single(createdAtStart = true) {
        UDPClient()
    }
}

