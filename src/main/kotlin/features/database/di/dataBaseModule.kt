package com.piedpiper.features.database.di

import com.piedpiper.common.Parameters
import com.zaxxer.hikari.HikariConfig
import com.zaxxer.hikari.HikariDataSource
import org.jetbrains.exposed.sql.Database
import org.jetbrains.exposed.sql.SchemaUtils
import org.jetbrains.exposed.sql.transactions.transaction
import org.koin.dsl.module
import com.piedpiper.features.user.data.models.UsersTable

val dataBaseModule = module {
    single<HikariDataSource> {
        val params = Parameters()

        val config = HikariConfig().apply {
            driverClassName = "org.postgresql.Driver"
            jdbcUrl =
                "jdbc:postgresql://${params.PG_HOST}:${params.PG_PORT}/${params.PG_DB}"
            username = params.PG_USER
            password = params.PG_PASSWORD
            maximumPoolSize = 10
            isAutoCommit = false
            transactionIsolation = "TRANSACTION_REPEATABLE_READ"
            validate()
        }

        HikariDataSource(config)
    }

    single<Database> {
        val dataSource: HikariDataSource = get()
        val db = Database.connect(dataSource)

        // Инициализация схемы БД
        transaction(db) {
            SchemaUtils.create(UsersTable)
        }

        db
    }
}