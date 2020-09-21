package com.mankovskaya.githubtest.ui.widget

import com.mankovskaya.githubtest.core.mvvm.BaseViewModel
import com.mankovskaya.githubtest.core.mvvm.StateReducer

data class ProgressState(
    val isLoading: Boolean,
    val error: ErrorState?
)

data class ErrorState(
    val message: String,
    val buttonAction: (() -> Unit)?
)

sealed class ProgressAction {
    object ProgressStarted : ProgressAction()
    object ProgressStopped : ProgressAction()
    data class ErrorOccurred(val error: ErrorState) : ProgressAction()
    object ButtonClicked : ProgressAction()
}

class ProgressViewModel : BaseViewModel<ProgressState, ProgressAction, Unit>(
    ProgressState(false, null)
) {
    override val stateReducer: StateReducer<ProgressState, ProgressAction> = ProgressStateReducer()

    inner class ProgressStateReducer : StateReducer<ProgressState, ProgressAction>() {
        override fun reduce(state: ProgressState, action: ProgressAction): ProgressState {
            return when (action) {
                is ProgressAction.ProgressStarted -> state.copy(isLoading = true)
                is ProgressAction.ProgressStopped -> state.copy(isLoading = false)
                is ProgressAction.ErrorOccurred -> state.copy(
                    isLoading = false,
                    error = action.error
                )
                is ProgressAction.ButtonClicked -> {
                    state.error?.buttonAction?.invoke()
                    state.copy(isLoading = true, error = null)
                }
            }
        }

    }

}