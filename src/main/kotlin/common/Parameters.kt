package com.piedpiper.common

data class Parameters(
    val DATABASE: String = System.getenv("DATABASE") ?: "PiedPiperBase",
    val MONGODB : String = System.getenv("MONGODB") ?: "mongodb://localhost:27017",
    val PG_HOST: String = System.getenv("PG_HOST") ?: "192.168.0.3",
    val PG_PORT: String = System.getenv("PG_PORT") ?: "5432",
    val PG_DB: String = System.getenv("PG_DB") ?: DATABASE,
    val PG_USER: String = System.getenv("PG_USER") ?: "postgres",
    val PG_PASSWORD: String = System.getenv("PG_PASSWORD") ?: "root",
)
