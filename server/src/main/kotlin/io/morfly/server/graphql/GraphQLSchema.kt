package io.morfly.server.graphql

import com.apurebase.kgraphql.GraphQL
import io.ktor.application.*
import io.morfly.server.domain.*
import kotlinx.datetime.Instant
import kotlinx.datetime.toInstant
import org.koin.ktor.ext.inject


fun Application.registerGraphQLSchema() {
    install(GraphQL) {
        val usersRepository by inject<UsersRepository>()
        val postsRepository by inject<PostsRepository>()

        playground = true

        schema {
            configure {
                useDefaultPrettyPrinter = true
            }

            stringScalar<Instant> {
                name = "Timestamp"
                deserialize = String::toInstant
                serialize = Instant::toString
            }

            type<User>()
            type<Post>()
            type<Comment>()

            usersSchema(usersRepository)
            postsSchema(postsRepository)
            commentsSchema(postsRepository)
        }
    }
}