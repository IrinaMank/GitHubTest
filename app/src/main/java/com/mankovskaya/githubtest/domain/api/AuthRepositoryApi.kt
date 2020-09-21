package com.mankovskaya.githubtest.domain.api

import io.reactivex.Single

interface AuthRepositoryApi {

    fun login(email: String, password: String): Single<Boolean>

    fun userLogged(): Boolean

    fun logout()

}