package com.mankovskaya.githubtest.model.feature

import com.mankovskaya.githubtest.core.mvvm.BaseStatefulViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer
import com.mankovskaya.githubtest.model.mock.AuthMockService
import com.mankovskaya.githubtest.model.repository.AuthRepository
import com.mankovskaya.githubtest.ui.widget.StateAction

data class LoginState(
    val email: String?,
    val password: String?,
    val error: String?
)

sealed class LoginEvent {
    object NavigateToRepositories: LoginEvent()
}

sealed class LoginAction {
    data class EmailChanged(val email: String) : LoginAction()
    data class PasswordChanged(val password: String) : LoginAction()
    object Login : LoginAction()
    object SuccessfulLogin : LoginAction()
    data class LoginError(val error: String) : LoginAction()
}

class LoginViewModel(
    private val authMockService: AuthMockService,
    private val authRepository: AuthRepository
) :
    BaseStatefulViewModel<LoginState, LoginAction, LoginEvent>(
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
                    sendStateAction(StateAction.ProgressStarted)
                    state.copy(error = null)
                }
                is LoginAction.SuccessfulLogin -> {
                    sendStateAction(StateAction.ProgressStopped)
                    state.also { navigateNext() }
                }
                is LoginAction.LoginError -> {
                    sendStateAction(StateAction.ProgressStopped)
                    state.copy(error = action.error)
                }
            }
        }
    }

    private fun login(email: String, password: String) {
        authRepository.login(email, password)
            .subscribe(
                {
                    reactOnAction(LoginAction.SuccessfulLogin)
                }, {
                    reactOnAction(
                        LoginAction.LoginError(
                            it.localizedMessage ?: "Error"
                        )
                    )
                }
            ).subscribeUntilDestroy()
    }

    private fun navigateNext() {
        postEvent(LoginEvent.NavigateToRepositories)
    }

}