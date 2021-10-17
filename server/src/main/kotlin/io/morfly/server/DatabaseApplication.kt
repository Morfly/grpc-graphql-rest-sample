package io.morfly.server

import io.ktor.application.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.morfly.server.data.DatabasePostsRepository
import io.morfly.server.data.DatabaseUsersRepository
import io.morfly.server.data.database.initDatabase
import io.morfly.server.data.database.populateDatabaseWithMockData
import io.morfly.server.domain.PostsRepository
import io.morfly.server.domain.UsersRepository
import io.morfly.server.rest.registerRESTRoutes
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.ktor.ext.Koin


const val DATABASE_SERVICE_PORT = 8050
const val DATABASE_SERVICE_ENDPOINT = "http://localhost:$DATABASE_SERVICE_PORT"


fun main() {
    embeddedServer(Netty, port = DATABASE_SERVICE_PORT, module = Application::databaseModule)
        .start(wait = true)
}

fun Application.databaseModule() = runBlocking {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        modules(databaseModule)
    }

    initDatabase()

    launch(Dispatchers.IO) {
        populateDatabaseWithMockData()
    }

    registerRESTRoutes()
}

val databaseModule = module {
    single<UsersRepository> { DatabaseUsersRepository() }
    single<PostsRepository> { DatabasePostsRepository() }
}