package com.mankovskaya.githubtest.domain.feature.repo

import com.mankovskaya.githubtest.core.android.ResourceManager
import com.mankovskaya.githubtest.core.mvvm.BaseProgressViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer
import com.mankovskaya.githubtest.core.paging.PagingTool
import com.mankovskaya.githubtest.data.model.RepositoryResult
import com.mankovskaya.githubtest.domain.api.AuthRepositoryApi
import com.mankovskaya.githubtest.domain.api.RepoRepositoryApi
import com.mankovskaya.githubtest.ui.common.getDefaultMessageId
import com.mankovskaya.githubtest.ui.widget.ErrorState
import com.mankovskaya.githubtest.ui.widget.ProgressAction

class RepositoriesViewModel(
    private val repoRepository: RepoRepositoryApi,
    private val authRepository: AuthRepositoryApi,
    private val resourceManager: ResourceManager
) : BaseProgressViewModel<RepositoriesSearchState, RepositorySearchAction, RepoEvent>(
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
                    sendStateAction(ProgressAction.ProgressStarted)
                    state
                }
                is RepositorySearchAction.RepositoryRefreshed -> {
                    sendStateAction(ProgressAction.ProgressStopped)
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
                    reactOnAction(RepositorySearchAction.LazyLoadChanged(false))
                    sendStateAction(ProgressAction.ErrorOccurred(
                        ErrorState(resourceManager.getString(it.getDefaultMessageId())) {
                            searchRepos(
                                searchInput
                            )
                        }
                    ))
                }
            ).subscribeUntilDestroyBy(TAG_SEARCH_REQUEST)

    }

    private fun listenToPagingUpdates() {
        if (disposables.anyInProgress(TAG_PAGING_EVENTS)) {
            disposables.unsubscribeBy(TAG_PAGING_EVENTS)
        }
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

    companion object {
        const val PAGE_SIZE = 10
        private const val TAG_SEARCH_REQUEST = "TAG_SEARCH_REQUEST"
        private const val TAG_PAGING_EVENTS = "TAG_PAGING_EVENTS"
    }

}