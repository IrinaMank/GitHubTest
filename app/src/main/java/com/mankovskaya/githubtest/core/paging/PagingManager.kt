package com.mankovskaya.githubtest.core.paging

import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.mankovskaya.githubtest.core.paging.PagingTool.dispatchNewPage

interface PagingManager {

    fun RecyclerView?.emitPaging(pageSize: Int) {
        if (this == null || adapter == null) return
        addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView, dx: Int, dy: Int) {
                val position = getLastVisibleItemPosition(recyclerView)
                val adapter = recyclerView.adapter ?: return
                if (adapter.itemCount == 0) {
                    dispatchNewPage(1, pageSize)
                    return
                }
                val updatePosition = adapter.itemCount - 1
                if (updatePosition in 1..position) {
                    val currentPageNumber = (position + 1) / pageSize
                    dispatchNewPage(currentPageNumber + 1, pageSize)
                }
            }
        })
        if (adapter?.itemCount == 0) {
            dispatchNewPage(1, pageSize)
        }
    }

    private fun getLastVisibleItemPosition(recyclerView: RecyclerView): Int {
        val layoutManager = recyclerView.layoutManager ?: return 0
        if (layoutManager is LinearLayoutManager) {
            return layoutManager.findLastCompletelyVisibleItemPosition()
        } else {
            throw UnsupportedOperationException("Pagination's created only for LinearLayoutManager")
        }
    }
}