package com.mankovskaya.githubtest.model.network

import com.fasterxml.jackson.annotation.JsonProperty

data class RepoSearchResponse(
    @JsonProperty("items") val items: List<RepositoryResponse>
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