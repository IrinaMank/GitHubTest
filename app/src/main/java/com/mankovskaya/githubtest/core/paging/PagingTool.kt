package com.mankovskaya.githubtest.core.paging

import com.jakewharton.rxrelay2.BehaviorRelay
import io.reactivex.Observable
import io.reactivex.Single
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

object PagingTool {

    private val pagingRelay: BehaviorRelay<PagingData> = BehaviorRelay.create()

    fun dispatchNewPage(pageNum: Int, pageSize: Int) {
        pagingRelay.accept(PagingData(pageNum, pageSize))
    }

    fun <T> observePaging(emitter: ((PagingData) -> Single<List<T>>)): Observable<List<T>> =
        pagingRelay
            .subscribeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .observeOn(Schedulers.io())
            .switchMapSingle { emitter.invoke(it) }
            .observeOn(AndroidSchedulers.mainThread())

    fun currentPage() = pagingRelay.value.pageNumber

}