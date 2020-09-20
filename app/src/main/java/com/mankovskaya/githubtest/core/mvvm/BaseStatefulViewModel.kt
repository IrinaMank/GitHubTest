package com.mankovskaya.githubtest.core.mvvm

import com.mankovskaya.githubtest.ui.widget.StateAction
import com.mankovskaya.githubtest.ui.widget.StateViewModel

abstract class BaseStatefulViewModel<State, Action, Event>(initialState: State) :
    BaseViewModel<State, Action, Event>(initialState) {
    val stateViewModel = StateViewModel()

    protected fun sendStateAction(action: StateAction) {
        stateViewModel.reactOnAction(action)
    }

}
