package com.mankovskaya.githubtest.model.feature

import com.mankovskaya.githubtest.core.mvvm.BaseStatefulViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer
import com.mankovskaya.githubtest.core.paging.PagingTool
import com.mankovskaya.githubtest.model.repository.RepoRepository
import com.mankovskaya.githubtest.ui.widget.ErrorState
import com.mankovskaya.githubtest.ui.widget.StateAction

class RepositoriesViewModel(
    private val repoRepository: RepoRepository
) : BaseStatefulViewModel<RepositoriesSearchState, RepositorySearchAction, Unit>(
    RepositoriesSearchState(
        searchQuery = null,
        repositories = listOf()
    )
) {
    override val stateReducer = RepositoryReducer()

    inner class RepositoryReducer :
        StateReducer<RepositoriesSearchState, RepositorySearchAction>() {
        override fun reduce(
            state: RepositoriesSearchState,
            action: RepositorySearchAction
        ): RepositoriesSearchState {
            return when (action) {
                is RepositorySearchAction.SearchChanged -> {
                    state.copy(searchQuery = action.searchInput).also {
                        searchRepos(action.searchInput)
                    }
                }
                is RepositorySearchAction.SearchStarted -> {
                    sendStateAction(StateAction.ProgressStarted)
                    state
                }
                is RepositorySearchAction.SearchError -> {
                    sendStateAction(StateAction.ErrorOccurred(ErrorState(action.message, null)))
                    state
                }
                is RepositorySearchAction.RepositoryRefreshed -> {
                    sendStateAction(StateAction.ProgressStopped)
                    state.copy(repositories = action.newList)
                }
            }
        }
    }

    private fun searchRepos(searchInput: String) {
        if (searchInput.isEmpty()) return
        if (disposables.anyInProgress(TAG_SEARCH_REQUEST)) {
            disposables.unsubscribeBy(TAG_SEARCH_REQUEST)
        }
        PagingTool.dispatchNewPage(1, PAGE_SIZE)
        repoRepository.search(searchInput)
            .doOnSubscribe { reactOnAction(RepositorySearchAction.SearchStarted) }
            .subscribe(
                { result ->
                    val newList = if (PagingTool.currentPage() > 1) {
                        getCurrentState().repositories.toMutableList().apply { addAll(result) }
                    } else {
                        result
                    }
                    reactOnAction(RepositorySearchAction.RepositoryRefreshed(newList))
                },
                {
                    reactOnAction(
                        RepositorySearchAction.SearchError(
                            it.localizedMessage ?: "Error"
                        )
                    )
                }
            ).subscribeUntilDestroyBy(TAG_SEARCH_REQUEST)

    }

    companion object {
        const val PAGE_SIZE = 10
        private const val TAG_SEARCH_REQUEST = "TAG_SEARCH_REQUEST"
    }

}