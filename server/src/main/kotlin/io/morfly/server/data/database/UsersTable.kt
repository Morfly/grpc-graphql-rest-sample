package io.morfly.server.data.database

import org.jetbrains.exposed.dao.id.IntIdTable


object UsersTable : IntIdTable() {
    val userName = varchar("username", length = 50)
    val displayName = varchar("display_name", length = 50)
    val avatarUrl = varchar("avatar_url", length = 100).nullable()
}