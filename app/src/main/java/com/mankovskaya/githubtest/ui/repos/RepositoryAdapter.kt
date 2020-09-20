package com.mankovskaya.githubtest.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.githubtest.R
import com.mankovskaya.githubtest.core.android.BaseRecyclerViewAdapter
import com.mankovskaya.githubtest.model.data.Repository

class RepositoryAdapter : BaseRecyclerViewAdapter() {

    private val items: MutableList<Repository> = mutableListOf()

    override fun getLayoutIdForPosition(position: Int): Int {
        val item = items[position]
        return R.layout.item_repository
    }

    override fun getViewModel(position: Int): Any? = items[position]

    override fun getItemCount() = items.size

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


}