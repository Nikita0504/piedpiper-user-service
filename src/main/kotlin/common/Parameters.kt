package com.piedpiper.common

data class Parameters(
    val DATABASE: String = System.getenv("DATABASE") ?: "PiedPiperBase",
    val MONGODB : String = System.getenv("MONGODB") ?: "mongodb://localhost:27017",
)
