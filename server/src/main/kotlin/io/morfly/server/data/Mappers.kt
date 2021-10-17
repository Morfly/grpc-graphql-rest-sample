package io.morfly.server.data

import io.morfly.server.data.database.CommentsTable
import io.morfly.server.data.database.PostsTable
import io.morfly.server.data.database.UsersTable
import io.morfly.server.domain.Comment
import io.morfly.server.domain.Post
import io.morfly.server.domain.User
import org.jetbrains.exposed.sql.Alias
import org.jetbrains.exposed.sql.ResultRow
import org.jetbrains.exposed.sql.Table


fun ResultRow.extractUser() = User(
    id = get(UsersTable.id).toString(),
    userName = get(UsersTable.userName),
    displayName = get(UsersTable.displayName),
    avatarUrl = get(UsersTable.avatarUrl)
)

fun <T : Table> ResultRow.extractUser(alias: Alias<T>) = User(
    id = get(alias[UsersTable.id]).toString(),
    userName = get(alias[UsersTable.userName]),
    displayName = get(alias[UsersTable.displayName]),
    avatarUrl = get(alias[UsersTable.avatarUrl])
)

fun ResultRow.extractPost() = Post(
    id = get(PostsTable.id).toString(),
    content = get(PostsTable.content),
    timestamp = get(PostsTable.timestamp),
    author = extractUser(),
    comments = emptyList()
)

inline fun ResultRow.extractComment(_extractUser: ResultRow.() -> User = { extractUser() }) = Comment(
    id = get(CommentsTable.id).toString(),
    postId = get(CommentsTable.postId).toString(),
    author = _extractUser(),
    content = get(CommentsTable.content),
    timestamp = get(CommentsTable.timestamp)
)

inline fun ResultRow.tryExtractComment(_extractUser: ResultRow.() -> User = { extractUser() }): Comment? =
    if (hasValue(CommentsTable.id) && getOrNull(CommentsTable.id) == null)
        null
    else extractComment(_extractUser)