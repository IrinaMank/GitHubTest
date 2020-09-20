package com.mankovskaya.githubtest.model.data

data class Repository(
    val id: Long,
    val ownerAvatarUrl: String?,
    val title: String,
    val description: String
)