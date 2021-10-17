package io.morfly.server.data.database

import io.ktor.application.*
import io.morfly.server.data.database.MockData.mockComments
import io.morfly.server.data.database.MockData.mockPosts
import io.morfly.server.data.database.MockData.mockUsers
import io.morfly.server.domain.*
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onEach
import kotlinx.datetime.Clock
import org.koin.ktor.ext.inject


private object MockData {

    val mockUsers = flow {
        emit(NewUser(userName = "alpha", displayName = "John Doe", avatarUrl = null))
        emit(NewUser(userName = "mega_coder", displayName = "Simon", avatarUrl = null))
        emit(NewUser(userName = "user123", displayName = "Jack Simpson", avatarUrl = null))
        emit(NewUser(userName = "walker101272", displayName = "Jared", avatarUrl = null))
    }

    val mockPosts = flow {
        emit(NewPost(authorId = "1", content = "Hello World!", timestamp = Clock.System.now()))
        emit(NewPost(authorId = "2", content = "Lorem ipsum", timestamp = Clock.System.now()))
        emit(NewPost(authorId = "3", content = "Empty post", timestamp = Clock.System.now()))
    }

    val mockComments = flow {
        emit(NewComment(postId = "1", authorId = "2", content = "Hola!", timestamp = Clock.System.now()))
        emit(NewComment(postId = "1", authorId = "3", content = "Greetings!", timestamp = Clock.System.now()))
        emit(NewComment(postId = "2", authorId = "3", content = "Hallå!", timestamp = Clock.System.now()))
    }
}

suspend fun Application.populateDatabaseWithMockData() {
    val usersRepository by inject<UsersRepository>()
    val postsRepository by inject<PostsRepository>()
    val timeDelayMillis = 10L

    mockUsers.collect(usersRepository::createUser)

    mockPosts
        .onEach { delay(timeDelayMillis) }
        .collect(postsRepository::createPost)

    mockComments
        .onEach { delay(timeDelayMillis) }
        .collect(postsRepository::createComment)
}