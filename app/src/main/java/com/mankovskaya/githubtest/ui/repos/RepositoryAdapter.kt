package com.mankovskaya.githubtest.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.core.android.BaseRecyclerViewAdapter
import com.mankovskaya.githubtest.core.paging.PagingAdapter
import com.mankovskaya.githubtest.data.model.Repository

class RepositoryAdapter : BaseRecyclerViewAdapter(), PagingAdapter {

    private val items: MutableList<Repository> = mutableListOf()

    override fun getLayoutIdForPosition(position: Int): Int {
        return if (isLoading && position == itemCount - 1) R.layout.item_lazy_load_footer else R.layout.item_repository
    }

    override fun getViewModel(position: Int): Any? =
        if (isLoading && position == itemCount - 1) null
        else items[position]

    override fun getItemCount() = if (isLoading) items.size + 1 else items.size

    fun setItems(items: List<Repository>) {
        val oldItems = this.items.toList()
        clear()
        addItems(items, false)
        val diffResult = DiffUtil.calculateDiff(RepositoryDiffUtil(items, oldItems))
        diffResult.dispatchUpdatesTo(this)
    }

    fun clear() = items.clear()

    fun addItems(newItems: List<Repository>, notify: Boolean) {
        if (newItems.isNotEmpty()) items.addAll(newItems)
        if (notify) notifyDataSetChanged()
    }

    override var isLoading: Boolean = false

    override fun showLoading(show: Boolean) {
        if (isLoading == show) return
        isLoading = show
        if (show) notifyItemInserted(items.size) else notifyItemRemoved(items.size)
    }


}