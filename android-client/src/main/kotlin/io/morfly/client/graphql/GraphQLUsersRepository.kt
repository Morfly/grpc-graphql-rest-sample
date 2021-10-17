package io.morfly.client.graphql

import com.apollographql.apollo.ApolloClient
import com.apollographql.apollo.api.Input
import com.apollographql.apollo.coroutines.await
import com.apollographql.apollo.exception.ApolloException
import io.morfly.client.CreateUserMutation
import io.morfly.client.DeleteUserMutation
import io.morfly.client.GetUserQuery
import io.morfly.client.ListUsersQuery
import io.morfly.client.domain.NewUser
import io.morfly.client.domain.User
import io.morfly.client.domain.UsersRepository


class GraphQLUsersRepository(
    private val client: ApolloClient
) : UsersRepository {

    override suspend fun listUsers(): List<User> {
        val response = try {
            client.query(ListUsersQuery()).await()
        } catch (e: ApolloException) {
            return emptyList()
        }

        val users = response.data?.users ?: emptyList()
        return users.map { User(it.id, it.userName, it.displayName, it.avatarUrl) }
    }

    override suspend fun getUser(userId: String): User? {
        val response = try {
            client.query(GetUserQuery(userId)).await()
        } catch (e: ApolloException) {
            return null
        }

        val user = response.data?.user ?: return null
        return user.run { User(id, userName, displayName, avatarUrl) }
    }

    override suspend fun createUser(user: NewUser): User? {
        val newUser = io.morfly.client.type.NewUser(
            userName = user.userName,
            displayName = user.displayName,
            avatarUrl = Input.optional(user.avatarUrl)
        )
        val mutation = CreateUserMutation(newUser)
        val response = try {
            client.mutate(mutation).await()
        } catch (e: ApolloException) {
            return null
        }

        val createdUser = response.data?.createUser ?: return null
        return createdUser.run { User(id, userName, displayName, avatarUrl) }
    }

    override suspend fun deleteUser(userId: String): Boolean {
        val mutation = DeleteUserMutation(userId)
        val response = try {
            client.mutate(mutation).await()
        } catch (e: ApolloException) {
            return false
        }

        return response.data?.deleteUser ?: false
    }
}