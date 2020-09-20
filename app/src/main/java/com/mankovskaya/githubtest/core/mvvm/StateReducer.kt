package com.mankovskaya.githubtest.core.mvvm


abstract class StateReducer<State, Action> {

    abstract fun reduce(state: State, action: Action): State
}
