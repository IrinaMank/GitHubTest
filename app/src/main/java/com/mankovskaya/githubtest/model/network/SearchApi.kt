package com.mankovskaya.githubtest.model.network

import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface SearchApi {

    @GET("/search/repositories")
    fun searchRepositories(@Query("q") query: String): Single<RepoSearchResponse>

}