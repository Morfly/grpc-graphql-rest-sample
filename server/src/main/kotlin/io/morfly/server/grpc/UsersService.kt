package io.morfly.server.grpc

import com.google.protobuf.Empty
import com.google.rpc.ErrorInfo
import io.grpc.Metadata
import io.grpc.Status
import io.grpc.StatusException
import io.grpc.protobuf.ProtoUtils
import io.morfly.server.domain.User
import io.morfly.server.domain.UsersRepository
import io.morfly.users.Users
import io.morfly.users.UsersServiceGrpcKt


class UsersService(
    private val usersRepository: UsersRepository
) : UsersServiceGrpcKt.UsersServiceCoroutineImplBase() {

    override suspend fun listUsers(request: Empty): Users.UserList =
        usersRepository
            .listUsers()
            .map(User::toGRPCUser)
            .toGRPCUserList()

    override suspend fun getUser(request: Users.GetUserRequest): Users.UserResponse {
        val user = usersRepository.getUser(request.userId)
            ?: throw userNotFoundException()

        return Users.UserResponse.newBuilder()
            .setUser(user.toGRPCUser())
            .build()
    }

    override suspend fun createUser(request: Users.NewUserRequest): Users.UserResponse {
        val createdUser = usersRepository.createUser(request.toNewUser())
            ?: throw userNotFoundException()

        return Users.UserResponse.newBuilder()
            .setUser(createdUser.toGRPCUser())
            .build()
    }

    override suspend fun deleteUser(request: Users.DeleteUserRequest): Users.UserDeletedResponse {
        val deleted = usersRepository.deleteUser(request.userId)

        return Users.UserDeletedResponse.newBuilder()
            .setDeleted(deleted)
            .build()
    }

    private fun userNotFoundException(): StatusException {
        val userNotFoundError = ErrorInfo.newBuilder()
            .setReason("User not found")
            .build()
        val errorDetail = Metadata()
        errorDetail.put(ProtoUtils.keyForProto(userNotFoundError), userNotFoundError)

        return StatusException(Status.NOT_FOUND, errorDetail)
    }
}