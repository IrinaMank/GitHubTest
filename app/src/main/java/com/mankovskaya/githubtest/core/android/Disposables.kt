package com.mankovskaya.githubtest.core.android

import io.reactivex.disposables.Disposable

class Disposables {

    private var disposables: MutableMap<String, MutableList<Disposable>> = mutableMapOf()

    fun add(disposable: Disposable) = add(DEFAULT_TAG, disposable)

    fun addAll(vararg disposables: Disposable) {
        disposables.forEach { add(it) }
    }

    fun add(tag: String, disposable: Disposable) = disposable.also {
        getDisposables(tag).add(it)
    }

    fun unsubscribeBy(tag: String) {
        disposables.remove(tag)?.disposeAll()
    }

    fun inProgress(disposable: Disposable?): Boolean {
        return disposable?.isDisposed?.not() ?: false
    }

    fun anyInProgress(tag: String): Boolean {
        return getDisposables(tag).any { inProgress(it) }
    }

    private fun getDisposables(tag: String): MutableList<Disposable> {
        return disposables.getOrPut(tag) { mutableListOf() }
    }

    private fun List<Disposable>.disposeAll() {
        forEach { dispose(it) }
    }

    private fun dispose(disposable: Disposable?) {
        disposable?.takeUnless { it.isDisposed }?.dispose()
    }


    fun disposeAll() = with(disposables.iterator()) {
        while (hasNext()) {
            next().value.disposeAll()
            remove()
        }
    }

    companion object {
        const val DEFAULT_TAG = "DEFAULT_TAG"
    }

}
