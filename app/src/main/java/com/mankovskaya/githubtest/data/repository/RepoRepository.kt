package com.mankovskaya.githubtest.data.repository

import com.mankovskaya.githubtest.core.android.BaseRepository
import com.mankovskaya.githubtest.core.paging.PagingTool
import com.mankovskaya.githubtest.data.model.Repository
import com.mankovskaya.githubtest.data.model.RepositoryResult
import com.mankovskaya.githubtest.data.network.api.SearchApi
import com.mankovskaya.githubtest.data.network.model.RepoSearchResponse
import com.mankovskaya.githubtest.domain.api.RepoRepositoryApi
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.Observable

class RepoRepository(
    private val searchApi: SearchApi,
    schedulersProvider: SchedulersProvider
) : BaseRepository(schedulersProvider), RepoRepositoryApi {

    override fun pagedSearch(query: String): Observable<RepositoryResult> =
        PagingTool.wrapToPagination { pagingData ->
            searchApi.searchRepositories(query, pagingData.pageNumber, pagingData.pageSize)
                .map { it to pagingData.pageNumber * pagingData.pageSize }
        }
            .map { (response, visibleItemsCount) ->
                response to (response.total <= visibleItemsCount)
            }
            .takeUntil { (_, allPagesLoaded) -> allPagesLoaded }
            .map { (response, allPagesLoaded) ->
                convertToDto(response, !allPagesLoaded)
            }
            .schedule()

    private fun convertToDto(response: RepoSearchResponse, canLoadMore: Boolean) =
        RepositoryResult(
            canLoadMore,
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