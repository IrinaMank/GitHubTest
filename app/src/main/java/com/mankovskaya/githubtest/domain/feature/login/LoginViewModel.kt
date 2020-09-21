package com.mankovskaya.githubtest.domain.feature.login

import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.core.android.ResourceManager
import com.mankovskaya.githubtest.core.mvvm.BaseProgressViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer
import com.mankovskaya.githubtest.domain.api.AuthRepositoryApi
import com.mankovskaya.githubtest.ui.common.getDefaultMessageId
import com.mankovskaya.githubtest.ui.widget.ErrorState
import com.mankovskaya.githubtest.ui.widget.ProgressAction

data class LoginState(
    val email: String?,
    val password: String?,
    val error: String?
)

sealed class LoginEvent {
    object NavigateToRepositories : LoginEvent()
}

sealed class LoginAction {
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    object Login : LoginAction()
    object SuccessfulLogin : LoginAction()
    data class LoginError(val error: String) : LoginAction()
}

class LoginViewModel(
    private val resourceManager: ResourceManager,
    private val authRepository: AuthRepositoryApi
) :
    BaseProgressViewModel<LoginState, LoginAction, LoginEvent>(
        LoginState(
            null,
            null,
            null
        )
    ) {
    override val stateReducer = LoginReducer()

    inner class LoginReducer : StateReducer<LoginState, LoginAction>() {
        override fun reduce(state: LoginState, action: LoginAction): LoginState {
            return when (action) {
                is LoginAction.EmailChanged -> {
                    state.copy(email = action.email, error = null)
                }
                is LoginAction.PasswordChanged -> {
                    state.copy(password = action.password, error = null)
                }
                is LoginAction.Login -> {
                    login(state.email!!, state.password!!)
                    sendStateAction(ProgressAction.ProgressStarted)
                    state.copy(error = null)
                }
                is LoginAction.SuccessfulLogin -> {
                    sendStateAction(ProgressAction.ProgressStopped)
                    state.also { navigateNext() }
                }
                is LoginAction.LoginError -> {
                    sendStateAction(ProgressAction.ProgressStopped)
                    state.copy(error = action.error)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        authRepository.login(email, password)
            .subscribe(
                { successfulLogin ->
                    if (successfulLogin) reactOnAction(LoginAction.SuccessfulLogin)
                    else reactOnAction(
                        LoginAction.LoginError(resourceManager.getString(R.string.login_wrong_credentials_error))
                    )
                }, {
                    sendStateAction(ProgressAction.ErrorOccurred(
                        ErrorState(resourceManager.getString(it.getDefaultMessageId())) {
                            login(
                                email,
                                password
                            )
                        }
                    ))
                }
            ).subscribeUntilDestroy()
    }

    private fun navigateNext() {
        postEvent(LoginEvent.NavigateToRepositories)
    }

}