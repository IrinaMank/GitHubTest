package com.mankovskaya.githubtest.core.paging

interface PagingAdapter {
    var isLoading: Boolean

    fun showLoading(show: Boolean)

}