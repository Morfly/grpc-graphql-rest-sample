package io.morfly.server

import io.grpc.ServerBuilder
import io.ktor.client.*
import io.ktor.client.engine.cio.*
import io.ktor.client.features.json.*
import io.ktor.client.features.logging.*
import io.morfly.server.data.InternalPostsRepository
import io.morfly.server.data.InternalUsersRepository
import io.morfly.server.domain.PostsRepository
import io.morfly.server.domain.UsersRepository
import io.morfly.server.grpc.CommentsService
import io.morfly.server.grpc.PostsService
import io.morfly.server.grpc.UsersService
import org.koin.core.component.KoinComponent
import org.koin.core.component.inject
import org.koin.core.context.startKoin
import org.koin.dsl.module


const val GRPC_SERVICE_PORT = 8040


fun main() {
    startKoin {
        modules(grpcModule)
    }

    GRPCApplication().main()
}


class GRPCApplication : KoinComponent {
    private val usersService by inject<UsersService>()
    private val postsService by inject<PostsService>()
    private val commentsService by inject<CommentsService>()

    fun main() {
        val server = ServerBuilder.forPort(GRPC_SERVICE_PORT)
            .addService(usersService)
            .addService(postsService)
            .addService(commentsService)
            .build()

        server.start()
        server.awaitTermination()
    }
}


val grpcModule = module {
    single {
        HttpClient(CIO) {
            install(Logging)
            install(JsonFeature)
        }
    }
    single<UsersRepository> { InternalUsersRepository(get()) }
    single<PostsRepository> { InternalPostsRepository(get()) }
    single { UsersService(get()) }
    single { PostsService(get()) }
    single { CommentsService(get()) }
}