package com.mankovskaya.githubtest.data.model

data class RepositoryResult(
    val canLoadMore: Boolean,
    val repositories: List<Repository>
)