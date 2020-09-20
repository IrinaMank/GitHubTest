package com.mankovskaya.githubtest.domain.feature.repo

import androidx.annotation.StringRes
import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.data.model.Repository

data class RepositoriesSearchState(
    val searchQuery: String?,
    val repositories: List<Repository>,
    val lazyLoad: Boolean,
    val authButton: AuthButtonState
)

sealed class RepositorySearchAction {
    data class SearchChanged(val searchInput: String) : RepositorySearchAction()
    object SearchStarted : RepositorySearchAction()
    data class LazyLoadChanged(val show: Boolean) : RepositorySearchAction()
    data class SearchError(val message: String) : RepositorySearchAction()
    data class RepositoryRefreshed(val newList: List<Repository>) : RepositorySearchAction()
    object AuthButtonPressed : RepositorySearchAction()
}

sealed class AuthButtonState(@StringRes open val textId: Int) {
    object LoginButton : AuthButtonState(R.string.repository_auth_button_login)
    object LogoutButton : AuthButtonState(R.string.repository_auth_button_logout)

    companion object {
        fun getState(isLogged: Boolean) = if (isLogged) LogoutButton else LoginButton
    }

}

sealed class RepoEvent {
    object NavigateToLogin : RepoEvent()
    object NavigateToWelcome : RepoEvent()
}
