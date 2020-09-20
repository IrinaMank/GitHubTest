package com.mankovskaya.githubtest.core.android

import io.reactivex.disposables.Disposable

internal class Disposables {

    private var disposables: MutableList<Disposable>? = mutableListOf()

    fun disposeAll() {
        disposables?.forEach { it.dispose() }
    }

    fun add(disposable: Disposable) {
        disposables?.apply {
            add(disposable)
        }
    }

}
