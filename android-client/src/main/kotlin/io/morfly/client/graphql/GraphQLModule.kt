package io.morfly.client.graphql

import com.apollographql.apollo.ApolloClient
import io.morfly.client.LOCALHOST
import io.morfly.client.domain.PostsRepository
import io.morfly.client.domain.UsersRepository
import io.morfly.client.type.CustomType
import io.morfly.client.ui.comments.CommentsViewModel
import io.morfly.client.ui.posts.PostsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module


val GraphQL = named("GraphQL")

val graphQLModule = module {
    single { TimestampScalarAdapter() }
    single {
        ApolloClient.builder()
            .serverUrl("http://$LOCALHOST:8090/graphql")
            .addCustomTypeAdapter(CustomType.TIMESTAMP, get<TimestampScalarAdapter>())
            .build()
    }
    single<PostsRepository>(GraphQL) { GraphQLPostsRepository(get()) }
    single<UsersRepository>(GraphQL) { GraphQLUsersRepository(get()) }
    viewModel(GraphQL) { PostsViewModel(get(GraphQL)) }
    viewModel(GraphQL) { CommentsViewModel(get(GraphQL)) }
}