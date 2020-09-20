package com.mankovskaya.githubtest.model.feature

import com.mankovskaya.githubtest.model.data.Repository

data class RepositoriesSearchState(
    val searchQuery: String?,
    val repositories: List<Repository>,
    val lazyLoad: Boolean
)

sealed class RepositorySearchAction {
    data class SearchChanged(val searchInput: String) : RepositorySearchAction()
    object SearchStarted: RepositorySearchAction()
    data class LazyLoadChanged(val show: Boolean) : RepositorySearchAction()
    data class SearchError(val message: String) : RepositorySearchAction()
    data class RepositoryRefreshed(val newList: List<Repository>) : RepositorySearchAction()
}

