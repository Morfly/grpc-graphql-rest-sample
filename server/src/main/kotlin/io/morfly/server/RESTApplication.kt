package io.morfly.server

import io.ktor.application.*
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.ktor.features.*
import io.ktor.serialization.*
import io.ktor.server.engine.*
import io.ktor.server.netty.*
import io.morfly.server.data.InternalPostsRepository
import io.morfly.server.data.InternalUsersRepository
import io.morfly.server.domain.PostsRepository
import io.morfly.server.domain.UsersRepository
import io.morfly.server.rest.registerRESTRoutes
import kotlinx.coroutines.runBlocking
import org.koin.dsl.module
import org.koin.ktor.ext.Koin


const val REST_SERVICE_PORT = 8080


fun main() {
    embeddedServer(
        Netty,
        port = REST_SERVICE_PORT,
        module = Application::restModule
    ).start(wait = true)
}

fun Application.restModule() = runBlocking {
    install(DefaultHeaders)
    install(CallLogging)
    install(ContentNegotiation) {
        json()
    }
    install(Koin) {
        modules(restModule)
    }

    registerRESTRoutes()
}

val restModule = module {
    single {
        HttpClient(CIO) {
            install(Logging)
            install(JsonFeature)
        }
    }
    single<UsersRepository> { InternalUsersRepository(get()) }
    single<PostsRepository> { InternalPostsRepository(get()) }
}