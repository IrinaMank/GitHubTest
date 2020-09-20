package com.mankovskaya.githubtest.model.feature

import com.mankovskaya.githubtest.model.data.Repository

data class RepositoriesSearchState(
    val searchQuery: String?,
    val repositoriesState: RepositoriesState
)

sealed class RepositoriesState {
    data class SucceedRepositories(val repos: List<Repository>): RepositoriesState()
    object EmptyRepositories : RepositoriesState()
}

sealed class RepositorySearchAction {
    data class SearchChanged(val searchInput: String) : RepositorySearchAction()
    object SearchStarted: RepositorySearchAction()
    data class SearchError(val message: String): RepositorySearchAction()
    data class RepositoryRefreshed(val newList: List<Repository>) : RepositorySearchAction()
}

