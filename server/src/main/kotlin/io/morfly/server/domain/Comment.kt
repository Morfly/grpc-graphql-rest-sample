package io.morfly.server.domain

import kotlinx.datetime.Instant
import kotlinx.serialization.Serializable


@Serializable
data class Comment(
    val id: String,
    val postId: String,
    val author: User,
    val content: String,
    val timestamp: Instant
)

@Serializable
data class NewComment(
    val postId: String,
    val authorId: String,
    val content: String,
    val timestamp: Instant
)