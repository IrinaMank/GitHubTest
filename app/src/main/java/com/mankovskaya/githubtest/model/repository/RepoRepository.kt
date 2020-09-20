package com.mankovskaya.githubtest.model.repository

import com.mankovskaya.githubtest.core.paging.PagingTool
import com.mankovskaya.githubtest.model.data.Repository
import com.mankovskaya.githubtest.model.data.RepositoryResult
import com.mankovskaya.githubtest.model.network.SearchApi
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.Observable

class RepoRepository(
    private val searchApi: SearchApi,
    schedulersProvider: SchedulersProvider
) : BaseRepository(schedulersProvider) {

    fun pagedSearch(query: String): Observable<RepositoryResult> =
        PagingTool
            .wrapToPagination { pagingData ->
                searchApi.searchRepositories(query, pagingData.pageNumber, pagingData.pageSize)
                    .map { it to pagingData.pageNumber * pagingData.pageSize }
            }
            .map { (response, visibleItemsCount) ->
                response to (response.total <= visibleItemsCount)
            }
            .takeUntil { (_, allPagesLoaded) -> allPagesLoaded }
            .map { (response, allPagesLoaded) ->
                RepositoryResult(
                    !allPagesLoaded,
                    response.items.map { repoResponse ->
                        Repository(
                            id = repoResponse.id,
                            ownerAvatarUrl = repoResponse.owner.avatar,
                            title = repoResponse.title,
                            description = repoResponse.description ?: ""
                        )
                    }
                )
            }
            .schedule()

}