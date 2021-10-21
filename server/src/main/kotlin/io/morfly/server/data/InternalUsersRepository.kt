package io.morfly.server.data

import io.ktor.client.*
import io.ktor.client.request.*
import io.ktor.client.statement.*
import io.ktor.http.*
import io.morfly.server.DATABASE_SERVICE_ENDPOINT
import io.morfly.server.domain.NewUser
import io.morfly.server.domain.User
import io.morfly.server.domain.UsersRepository


class InternalUsersRepository(
    private val client: HttpClient
) : UsersRepository {

    override suspend fun listUsers(): List<User> =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/user") {
            method = HttpMethod.Get
        }

    override suspend fun getUser(userId: String): User? =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/user/$userId") {
            method = HttpMethod.Get
        }

    override suspend fun createUser(user: NewUser): User? =
        client.request("$DATABASE_SERVICE_ENDPOINT/rest/user") {
            method = HttpMethod.Post
            header(HttpHeaders.ContentType, ContentType.Application.Json)
            body = user
        }

    override suspend fun deleteUser(userId: String): Boolean {
        val response: HttpResponse =
            client.request("$DATABASE_SERVICE_ENDPOINT/rest/user/$userId") {
                method = HttpMethod.Delete
            }
        return response.status == HttpStatusCode.Accepted
    }

}