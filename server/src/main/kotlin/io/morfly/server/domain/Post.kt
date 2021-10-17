package io.morfly.server.domain

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class Post(
    val id: String,
    val author: User,
    val content: String,
    val timestamp: Instant,
    val comments: List<Comment>
)

@Serializable
class NewPost(
    val authorId: String,
    val content: String,
    val timestamp: Instant
)