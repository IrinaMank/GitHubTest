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

    fun <T> wrapToPagination(emitter: ((PagingData) -> Single<T>)): Observable<T> =
        pagingRelay
            .subscribeOn(AndroidSchedulers.mainThread())
            .distinctUntilChanged()
            .observeOn(Schedulers.io())
            .switchMapSingle { emitter.invoke(it) }
            .observeOn(AndroidSchedulers.mainThread())

    fun observePaging() = pagingRelay
        .subscribeOn(AndroidSchedulers.mainThread())
        .observeOn(AndroidSchedulers.mainThread())

    fun currentPage() = pagingRelay.value.pageNumber

}