package io.morfly.server.graphql

import com.apurebase.kgraphql.schema.dsl.SchemaBuilder
import io.morfly.server.domain.NewUser
import io.morfly.server.domain.UsersRepository


fun SchemaBuilder.usersSchema(usersRepository: UsersRepository) {
    query("users") {
        resolver { -> usersRepository.listUsers() }
    }

    query("user") {
        resolver { userId: String -> usersRepository.getUser(userId) }
    }

    mutation("createUser") {
        resolver { user: NewUser -> usersRepository.createUser(user) }
    }

    mutation("deleteUser") {
        resolver { userId: String -> usersRepository.deleteUser(userId) }
    }
}