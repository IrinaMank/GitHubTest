package com.mankovskaya.githubtest.system.scheduler

import io.reactivex.*

interface SchedulersProvider {

    fun ui(): Scheduler

    fun computation(): Scheduler

    fun trampoline(): Scheduler

    fun newThread(): Scheduler

    fun io(): Scheduler

    fun <T> applyToObservable(): ObservableTransformer<T, T> = ObservableTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    fun <T> applyToFlowable(): FlowableTransformer<T, T> = FlowableTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    fun <T> applyToSingle(): SingleTransformer<T, T> = SingleTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    fun <T> applyToMaybe(): MaybeTransformer<T, T> = MaybeTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

    fun applyToCompletable(): CompletableTransformer = CompletableTransformer {
        it.subscribeOn(io()).observeOn(ui())
    }

}
