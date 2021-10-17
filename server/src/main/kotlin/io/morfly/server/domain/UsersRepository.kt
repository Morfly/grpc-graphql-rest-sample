package io.morfly.server.domain


interface UsersRepository {

    suspend fun listUsers(): List<User>

    suspend fun getUser(userId: String): User?

    suspend fun createUser(user: NewUser): User?

    suspend fun deleteUser(userId: String): Boolean
}