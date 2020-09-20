package com.mankovskaya.githubtest.model.network

import io.reactivex.Completable
import io.reactivex.Single
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface AuthApi {

    @GET("/user")
    fun getUser(): Completable //ignore user cause we don't really need him

}