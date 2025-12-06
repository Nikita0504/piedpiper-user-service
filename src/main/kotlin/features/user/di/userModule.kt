package com.piedpiper.features.user.di

import com.piedpiper.features.user.data.repository.UserDataRepository
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.module
import com.piedpiper.features.user.data.services.UserService
import org.koin.core.module.dsl.bind

val userModule = module {
    singleOf(::UserService) { bind<UserDataRepository>() }}