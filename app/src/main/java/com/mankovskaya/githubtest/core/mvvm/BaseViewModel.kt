package com.mankovskaya.githubtest.core.mvvm

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.distinctUntilChanged
import com.mankovskaya.githubtest.core.android.Disposables
import io.reactivex.disposables.Disposable

abstract class BaseViewModel<State, Action, Event>(
    private val initialState: State
) : ViewModel() {
    private val stateRelay: MutableLiveData<State> = MutableLiveData(initialState)
    protected val disposables = Disposables()
    protected abstract val stateReducer: StateReducer<State, Action>
    val liveEvent = LiveEvent<Event>()

    override fun onCleared() {
        disposables.disposeAll()
        super.onCleared()
    }

    fun getStateRelay(): LiveData<State> = stateRelay.distinctUntilChanged()

    fun getCurrentState(): State = stateRelay.value ?: initialState

    fun reactOnAction(action: Action) {
        stateRelay.value = stateReducer.reduce(stateRelay.value ?: initialState, action)
    }

    protected fun postEvent(event: Event) {
        liveEvent.postValue(event)
    }

    protected fun Disposable.subscribeUntilDestroy() {
        disposables.add(this)
    }

    protected fun Disposable.subscribeUntilDestroyBy(tag: String) {
        disposables.add(tag, this)
    }

}
