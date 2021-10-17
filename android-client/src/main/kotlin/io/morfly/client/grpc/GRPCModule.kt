package io.morfly.client.grpc

import io.grpc.Channel
import io.grpc.ManagedChannelBuilder
import io.morfly.client.LOCALHOST
import io.morfly.client.domain.PostsRepository
import io.morfly.client.domain.UsersRepository
import io.morfly.client.ui.comments.CommentsViewModel
import io.morfly.client.ui.posts.PostsViewModel
import io.morfly.comments.CommentsServiceGrpcKt
import io.morfly.posts.PostsServiceGrpcKt
import io.morfly.users.UsersServiceGrpcKt
import kotlinx.serialization.ExperimentalSerializationApi
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import java.net.URL


val gRPC = named("gRPC")

@OptIn(ExperimentalSerializationApi::class)
val gRPCModule = module {
    single<Channel> {
        ManagedChannelBuilder
            .forAddress(LOCALHOST, 8040)
            .usePlaintext()
            .build()
    }
    single { UsersServiceGrpcKt.UsersServiceCoroutineStub(get()) }
    single { PostsServiceGrpcKt.PostsServiceCoroutineStub(get()) }
    single { CommentsServiceGrpcKt.CommentsServiceCoroutineStub(get()) }
    single<PostsRepository>(gRPC) { GRPCPostsRepository(get(), get()) }
    single<UsersRepository>(gRPC) { GRPCUsersRepository(get()) }
    viewModel(gRPC) { PostsViewModel(get(gRPC)) }
    viewModel(gRPC) { CommentsViewModel(get(gRPC)) }
}