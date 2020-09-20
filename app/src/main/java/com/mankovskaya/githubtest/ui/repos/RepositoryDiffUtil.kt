package com.mankovskaya.githubtest.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.githubtest.model.data.Repository

class RepositoryDiffUtil(
    private val newRepositories: List<Repository>,
    private val oldRepositories: List<Repository>
) :
    DiffUtil.Callback() {

    override fun getOldListSize(): Int {
        return oldRepositories.size
    }

    override fun getNewListSize(): Int {
        return newRepositories.size
    }

    override fun areItemsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldRepositories[oldItemPosition] === newRepositories[newItemPosition]
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return when {
            oldRepositories[oldItemPosition]::class != newRepositories[newItemPosition]::class -> false
            oldRepositories[oldItemPosition].id == newRepositories[newItemPosition].id -> true
            else -> false
        }
    }

    override fun getChangePayload(oldItemPosition: Int, newItemPosition: Int): Any? {
        return super.getChangePayload(oldItemPosition, newItemPosition)
    }

}