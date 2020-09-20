package com.mankovskaya.githubtest.domain.feature.repo

import com.mankovskaya.githubtest.core.mvvm.BaseStatefulViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer
import com.mankovskaya.githubtest.core.paging.PagingTool
import com.mankovskaya.githubtest.data.model.RepositoryResult
import com.mankovskaya.githubtest.data.repository.AuthRepository
import com.mankovskaya.githubtest.data.repository.RepoRepository
import com.mankovskaya.githubtest.ui.widget.ErrorState
import com.mankovskaya.githubtest.ui.widget.StateAction

class RepositoriesViewModel(
    private val repoRepository: RepoRepository,
    private val authRepository: AuthRepository
) : BaseStatefulViewModel<RepositoriesSearchState, RepositorySearchAction, RepoEvent>(
    RepositoriesSearchState(
        searchQuery = null,
        repositories = listOf(),
        lazyLoad = false,
        authButton = AuthButtonState.getState(
            authRepository.userLogged()
        )
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
                is RepositorySearchAction.LazyLoadChanged -> {
                    state.copy(lazyLoad = action.show)
                }
                is RepositorySearchAction.AuthButtonPressed -> {
                    if (authRepository.userLogged()) logOut() else login()
                    state
                }
            }
        }
    }

    private fun logOut() {
        authRepository.logout()
        postEvent(RepoEvent.NavigateToWelcome)
    }

    private fun login() {
        postEvent(RepoEvent.NavigateToLogin)
    }

    private fun searchRepos(searchInput: String) {
        if (searchInput.isEmpty()) return
        terminatePreviousSearchRequests()
        listenToPagingUpdates()
        repoRepository.pagedSearch(searchInput)
            .doOnSubscribe { reactOnAction(RepositorySearchAction.SearchStarted) }
            .subscribe(
                { result ->
                    processResult(result)
                },
                {
                    processError(it)
                }
            ).subscribeUntilDestroyBy(TAG_SEARCH_REQUEST)

    }

    private fun listenToPagingUpdates() {
        PagingTool.observePaging()
            .subscribe {
                if (it.pageNumber > 1) reactOnAction(RepositorySearchAction.LazyLoadChanged(true))
            }
            .subscribeUntilDestroyBy(TAG_PAGING_EVENTS)
    }

    private fun terminatePreviousSearchRequests() {
        if (disposables.anyInProgress(TAG_SEARCH_REQUEST)) {
            disposables.unsubscribeBy(TAG_SEARCH_REQUEST)
        }
        PagingTool.dispatchNewPage(
            1,
            PAGE_SIZE
        )
    }

    private fun processResult(result: RepositoryResult) {
        reactOnAction(RepositorySearchAction.LazyLoadChanged(false))
        if (!result.canLoadMore) disposables.unsubscribeBy(TAG_PAGING_EVENTS)
        val newList = if (PagingTool.currentPage() > 1) {
            getCurrentState().repositories.toMutableList().apply { addAll(result.repositories) }
        } else {
            result.repositories
        }
        reactOnAction(RepositorySearchAction.RepositoryRefreshed(newList))
    }

    private fun processError(error: Throwable) {
        reactOnAction(RepositorySearchAction.LazyLoadChanged(false))
        reactOnAction(
            RepositorySearchAction.SearchError(
                error.localizedMessage ?: "Error"
            )
        )
    }

    companion object {
        const val PAGE_SIZE = 10
        private const val TAG_SEARCH_REQUEST = "TAG_SEARCH_REQUEST"
        private const val TAG_PAGING_EVENTS = "TAG_PAGING_EVENTS"
    }

}