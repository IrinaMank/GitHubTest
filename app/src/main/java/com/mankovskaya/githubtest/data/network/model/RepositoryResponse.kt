package com.mankovskaya.githubtest.data.network.model

import com.fasterxml.jackson.annotation.JsonProperty

data class RepoSearchResponse(
    @JsonProperty("items") val items: List<RepositoryResponse>,
    @JsonProperty("total_count") val total: Int
)

data class RepositoryResponse(
    @JsonProperty("owner") val owner: OwnerResponse,
    @JsonProperty("id") val id: Long,
    @JsonProperty("name") val title: String,
    @JsonProperty("description") val description: String?
)

data class OwnerResponse(
    @JsonProperty("avatar_url") val avatar: String?
)