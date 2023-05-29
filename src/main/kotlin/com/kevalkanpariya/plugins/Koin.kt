package com.kevalkanpariya.plugins

import com.kevalkanpariya.di.getKoinModule
import io.ktor.server.application.*
import org.koin.ktor.plugin.Koin

fun Application.configureKoin() {
    install(Koin){
        modules(getKoinModule())
    }
}