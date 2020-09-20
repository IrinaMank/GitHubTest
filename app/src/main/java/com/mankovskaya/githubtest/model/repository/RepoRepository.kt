package com.mankovskaya.githubtest.model.repository

import com.mankovskaya.githubtest.core.paging.PagingTool
import com.mankovskaya.githubtest.model.data.Repository
import com.mankovskaya.githubtest.model.network.SearchApi
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.Observable

class RepoRepository(
    private val searchApi: SearchApi,
    schedulersProvider: SchedulersProvider
) : BaseRepository(schedulersProvider) {

    fun search(query: String): Observable<List<Repository>> =
        PagingTool.wrapToPagination {
            searchApi.searchRepositories(query, it.pageNumber, it.pageSize).map { response ->
                response.items.map { repoResponse ->
                    Repository(
                        id = repoResponse.id,
                        ownerAvatarUrl = repoResponse.owner.avatar,
                        title = repoResponse.title,
                        description = repoResponse.description ?: ""
                    )
                }
            }
        }
            .schedule()

}