package com.mankovskaya.githubtest.data.model

data class Repository(
    val id: Long,
    val ownerAvatarUrl: String?,
    val title: String,
    val description: String
)