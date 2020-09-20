package com.mankovskaya.githubtest.model.repository

import com.mankovskaya.githubtest.model.data.Repository
import com.mankovskaya.githubtest.model.network.SearchApi
import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.Single

class RepoRepository(
    private val searchApi: SearchApi,
    schedulersProvider: SchedulersProvider
) : BaseRepository(schedulersProvider) {

    fun search(query: String): Single<List<Repository>> =
        searchApi.searchRepositories(query).map {
            it.items.map {
                Repository(
                    id = it.id,
                    ownerAvatarUrl = it.owner.avatar,
                    title = it.title,
                    description = it.description ?: ""
                )
            }
        }
            .schedule()

}