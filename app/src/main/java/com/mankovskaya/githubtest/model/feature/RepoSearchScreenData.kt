package com.mankovskaya.githubtest.model.feature

import com.mankovskaya.githubtest.model.data.Repository

data class RepositoriesSearchState(
    val searchQuery: String?,
    val repositories: List<Repository>
)

sealed class RepositorySearchAction {
    data class SearchChanged(val searchInput: String) : RepositorySearchAction()
    object SearchStarted: RepositorySearchAction()
    data class SearchError(val message: String): RepositorySearchAction()
    data class RepositoryRefreshed(val newList: List<Repository>) : RepositorySearchAction()
}

