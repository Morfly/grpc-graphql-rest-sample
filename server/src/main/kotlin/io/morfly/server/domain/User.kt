package io.morfly.server.domain

import kotlinx.serialization.Serializable


@Serializable
data class User(
    val id: String,
    val userName: String,
    val displayName: String,
    val avatarUrl: String?,
)

@Serializable
data class NewUser(
    val userName: String,
    val displayName: String,
    val avatarUrl: String?,
)