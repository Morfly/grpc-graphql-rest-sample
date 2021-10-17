package io.morfly.client.rest

import io.morfly.client.domain.NewUser
import io.morfly.client.domain.User
import io.morfly.client.domain.UsersRepository


class RESTUsersRepository(
    private val service: RESTService
) : UsersRepository {

    override suspend fun listUsers(): List<User> =
        service.listUsers()

    override suspend fun getUser(userId: String): User? =
        service.getUser(userId)

    override suspend fun createUser(user: NewUser): User? =
        service.createUser(user)

    override suspend fun deleteUser(userId: String): Boolean =
        service.deleteUser(userId)
}