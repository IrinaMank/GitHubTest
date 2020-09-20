package com.mankovskaya.githubtest.core.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.mankovskaya.githubtest.core.android.Disposables
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<State, Action, Event>(
    private val initialState: State
) : ViewModel() {
    private val stateRelay: MutableLiveData<State> = MutableLiveData(initialState)
    private val disposables = Disposables()
    protected abstract val stateReducer: StateReducer<State, Action>
    val liveEvent = LiveEvent<Event>()

    override fun onCleared() {
        disposables.disposeAll()
        super.onCleared()
    }

    fun getStateRelay(): LiveData<State> = stateRelay

    fun reactOnAction(action: Action) {
        stateRelay.value = stateReducer.reduce(stateRelay.value ?: initialState, action)
    }

    protected fun postEvent(event: Event) {
        liveEvent.postValue(event)
    }

    protected fun Disposable.subscribeUntilDestroy() {
        disposables.add(this)
    }

}
