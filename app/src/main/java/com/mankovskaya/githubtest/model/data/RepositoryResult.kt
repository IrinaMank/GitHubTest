package com.mankovskaya.githubtest.model.data

data class RepositoryResult(
    val canLoadMore: Boolean,
    val repositories: List<Repository>
)