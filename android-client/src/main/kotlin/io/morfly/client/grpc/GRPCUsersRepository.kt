package io.morfly.client.grpc

import com.google.protobuf.Empty
import io.morfly.client.domain.NewUser
import io.morfly.client.domain.User
import io.morfly.client.domain.UsersRepository
import io.morfly.users.Users
import io.morfly.users.UsersServiceGrpcKt


class GRPCUsersRepository(
    private val usersServiceStub: UsersServiceGrpcKt.UsersServiceCoroutineStub
) : UsersRepository {

    override suspend fun listUsers(): List<User> {
        val request = Empty.newBuilder().build()
        val users = usersServiceStub.listUsers(request).usersList

        return users.map { it.toUser() }
    }

    override suspend fun getUser(userId: String): User? {
        val request = Users.GetUserRequest.newBuilder()
            .setUserId(userId)
            .build()

        val user = usersServiceStub.getUser(request).user ?: return null
        return user.toUser()
    }

    override suspend fun createUser(user: NewUser): User? {
        val request = user.toGRPCNewUserRequest()

        val createdUser = usersServiceStub.createUser(request).user ?: return null
        return createdUser.toUser()
    }

    override suspend fun deleteUser(userId: String): Boolean {
        val request = Users.DeleteUserRequest.newBuilder()
            .setUserId(userId)
            .build()

        return usersServiceStub.deleteUser(request).deleted
    }
}