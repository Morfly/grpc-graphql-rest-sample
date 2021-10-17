package io.morfly.server.data.database

import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils.create
import org.jetbrains.exposed.sql.transactions.transaction


fun initDatabase() {
    Database.connect(hikari())

    transaction {
        create(UsersTable, PostsTable, CommentsTable)
    }
}

private fun hikari(): HikariDataSource {
    val config = HikariConfig().apply {
        driverClassName = "org.h2.Driver"
        jdbcUrl = "jdbc:h2:mem:sample"
    }
    return HikariDataSource(config)
}
