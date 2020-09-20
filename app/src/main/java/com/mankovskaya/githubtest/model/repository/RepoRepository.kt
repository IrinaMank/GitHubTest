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
        PagingTool.observePaging {
            searchApi.searchRepositories(query, it.pageNumber, it.pageSize).map {
                it.items.map {
                    Repository(
                        id = it.id,
                        ownerAvatarUrl = it.owner.avatar,
                        title = it.title,
                        description = it.description ?: ""
                    )
                }
            }
        }
            .schedule()

}