package com.mankovskaya.githubtest.model.feature

import com.jakewharton.rxrelay2.BehaviorRelay
import com.mankovskaya.githubtest.core.mvvm.BaseStatefulViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer
import com.mankovskaya.githubtest.model.repository.RepoRepository
import com.mankovskaya.githubtest.ui.widget.ErrorState
import com.mankovskaya.githubtest.ui.widget.StateAction
import io.reactivex.android.schedulers.AndroidSchedulers
import java.util.concurrent.TimeUnit

class RepositoriesViewModel(
    private val repoRepository: RepoRepository
) : BaseStatefulViewModel<RepositoriesSearchState, RepositorySearchAction, Unit>(
    RepositoriesSearchState(
        searchQuery = null,
        repositoriesState = RepositoriesState.EmptyRepositories
    )
) {
    private val searchRelay = BehaviorRelay.create<String>()
    override val stateReducer = RepositoryReducer()

    init {
        searchRelay.debounce(1, TimeUnit.SECONDS)
            .observeOn(AndroidSchedulers.mainThread())
            .subscribeOn(AndroidSchedulers.mainThread())
            .subscribe({ searchRepos(it) }, {})
            .subscribeUntilDestroy()
    }

    inner class RepositoryReducer :
        StateReducer<RepositoriesSearchState, RepositorySearchAction>() {
        override fun reduce(
            state: RepositoriesSearchState,
            action: RepositorySearchAction
        ): RepositoriesSearchState {
            return when (action) {
                is RepositorySearchAction.SearchChanged -> {
                    state.copy(searchQuery = action.searchInput).also {
                        searchRelay.accept(action.searchInput)
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
                    if (action.newList.isEmpty()) {
                        state.copy(repositoriesState = RepositoriesState.EmptyRepositories)
                    } else {
                        state.copy(repositoriesState = RepositoriesState.SucceedRepositories(action.newList))
                    }
                }
            }
        }
    }

    private fun searchRepos(searchInput: String) {
        if (searchInput.isEmpty()) return
        repoRepository.search(searchInput)
            .doOnSubscribe { reactOnAction(RepositorySearchAction.SearchStarted) }
            .subscribe(
                {
                    reactOnAction(RepositorySearchAction.RepositoryRefreshed(it))
                },
                {
                    reactOnAction(RepositorySearchAction.SearchError(it.localizedMessage ?: "Error"))
                }
            ).subscribeUntilDestroy()

    }

}