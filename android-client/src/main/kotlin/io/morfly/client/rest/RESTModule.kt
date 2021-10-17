package io.morfly.client.rest

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import io.morfly.client.domain.PostsRepository
import io.morfly.client.domain.UsersRepository
import io.morfly.client.ui.comments.CommentsViewModel
import io.morfly.client.ui.posts.PostsViewModel
import kotlinx.serialization.ExperimentalSerializationApi
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.core.qualifier.named
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.create


val REST = named("REST")

@OptIn(ExperimentalSerializationApi::class)
val restModule = module {
    single {
        val loggingInterceptor = HttpLoggingInterceptor()
        loggingInterceptor.level = HttpLoggingInterceptor.Level.BODY

        OkHttpClient.Builder()
            .addNetworkInterceptor(loggingInterceptor)
            .build()
    }
    single<RESTService> {
        val contentType = "application/json".toMediaType()
        Retrofit.Builder()
            .baseUrl(RESTService.BASE_URL)
            .client(get())
            .addConverterFactory(Json.asConverterFactory(contentType))
            .build()
            .create()
    }
    single<PostsRepository>(REST) { RESTPostsRepository(get()) }
    single<UsersRepository>(REST) { RESTUsersRepository(get()) }
    viewModel(REST) { PostsViewModel(get(REST)) }
    viewModel(REST) { CommentsViewModel(get(REST)) }
}