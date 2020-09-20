package com.mankovskaya.githubtest.model.repository

import com.mankovskaya.githubtest.system.scheduler.SchedulersProvider
import io.reactivex.*

open class BaseRepository(
        val schedulers: SchedulersProvider
) {

    protected fun <T> Observable<T>.schedule(): Observable<T> = this.compose(schedulers.applyToObservable())

    protected fun <T> Flowable<T>.schedule(): Flowable<T> = this.compose(schedulers.applyToFlowable())

    protected fun <T> Single<T>.schedule(): Single<T> = this.compose(schedulers.applyToSingle())

    protected fun <T> Maybe<T>.schedule(): Maybe<T> = this.compose(schedulers.applyToMaybe())

    protected fun Completable.schedule(): Completable = this.compose(schedulers.applyToCompletable())

}
