package com.mankovskaya.githubtest.core.mvvm

import com.mankovskaya.githubtest.ui.widget.ProgressAction
import com.mankovskaya.githubtest.ui.widget.ProgressViewModel

abstract class BaseProgressViewModel<State, Action, Event>(initialState: State) :
    BaseViewModel<State, Action, Event>(initialState) {
    val stateViewModel = ProgressViewModel()

    protected fun sendStateAction(action: ProgressAction) {
        stateViewModel.reactOnAction(action)
    }

}
