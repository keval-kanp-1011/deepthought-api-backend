package com.kevalkanpariya.di

import com.kevalkanpariya.data.repository.event.EventRepository
import com.kevalkanpariya.data.repository.event.EventRepositoryImpl
import com.kevalkanpariya.data.repository.aws.AmazonFileStorage
import com.kevalkanpariya.data.repository.aws.FileStorage
import com.kevalkanpariya.services.EventService
import org.koin.dsl.module
import org.litote.kmongo.coroutine.coroutine
import org.litote.kmongo.reactivestreams.KMongo

fun getKoinModule() = module {

    single {
        KMongo.createClient("your-client-id")
            .coroutine
            .getDatabase("deepthought-api-backend")
    }

    single<FileStorage> {
        AmazonFileStorage()
    }

    /*Repository Instance*/
    single<EventRepository> {
        EventRepositoryImpl(get())
    }



    /*Service Instance*/
    single { EventService(get()) }


}