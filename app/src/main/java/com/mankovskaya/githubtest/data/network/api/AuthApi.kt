package com.mankovskaya.githubtest.data.network.api

import io.reactivex.Completable
import retrofit2.http.GET

interface AuthApi {

    @GET("/user")
    fun getUser(): Completable //ignore user cause we don't really need him

}