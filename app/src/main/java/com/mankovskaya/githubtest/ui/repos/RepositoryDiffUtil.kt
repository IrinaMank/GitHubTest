package com.mankovskaya.githubtest.ui.repos

import androidx.recyclerview.widget.DiffUtil
import com.mankovskaya.githubtest.data.model.Repository

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
        if (oldRepositories[oldItemPosition] === newRepositories[newItemPosition]) return true
        return oldRepositories[oldItemPosition].id == newRepositories[newItemPosition].id
    }

    override fun areContentsTheSame(
        oldItemPosition: Int,
        newItemPosition: Int
    ): Boolean {
        return oldRepositories[oldItemPosition] == newRepositories[newItemPosition]
    }

}