package com.mankovskaya.githubtest.domain.api

import com.mankovskaya.githubtest.data.model.RepositoryResult
import io.reactivex.Observable

interface RepoRepositoryApi {

    fun pagedSearch(query: String): Observable<RepositoryResult>

}