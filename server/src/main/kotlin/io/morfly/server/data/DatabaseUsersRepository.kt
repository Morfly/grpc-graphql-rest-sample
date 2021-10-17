package io.morfly.server.data

import io.morfly.server.data.database.UsersTable
import io.morfly.server.domain.NewUser
import io.morfly.server.domain.User
import io.morfly.server.domain.UsersRepository
import org.jetbrains.exposed.sql.deleteWhere
import org.jetbrains.exposed.sql.insert
import org.jetbrains.exposed.sql.select
import org.jetbrains.exposed.sql.selectAll
import org.jetbrains.exposed.sql.transactions.transaction


class DatabaseUsersRepository : UsersRepository {

    override suspend fun listUsers(): List<User> = transaction {
        UsersTable
            .selectAll()
            .map { it.extractUser() }
    }

    override suspend fun getUser(userId: String): User? {
        val intUserId = userId.toIntOrNull() ?: return null

        return transaction {
            UsersTable
                .select { UsersTable.id eq intUserId }
                .singleOrNull()
                ?.extractUser()
        }
    }

    override suspend fun createUser(user: NewUser): User? = transaction {
        val inserted = UsersTable.insert {
            it[userName] = user.userName
            it[displayName] = user.displayName
            it[avatarUrl] = user.avatarUrl
        }.resultedValues?.firstOrNull()

        inserted?.extractUser()
    }

    override suspend fun deleteUser(userId: String): Boolean {
        // Not allowed to delete an admin user.
        if (userId == "1") return false

        val intUserId = userId.toIntOrNull() ?: return false

        return transaction {
            val numDeleted: Int = UsersTable
                .deleteWhere { UsersTable.id eq intUserId }
            numDeleted > 0
        }
    }
}