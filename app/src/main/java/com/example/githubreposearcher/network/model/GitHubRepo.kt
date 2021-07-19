package com.example.githubreposearcher.network.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable
import com.example.githubreposearcher.domain.model.GitHubRepo as DomainGitHubRepo

@Serializable
data class GitHubRepo(
    @SerialName("full_name") val fullName: String,
    @SerialName("description") val description: String?,
    @SerialName("html_url") val htmlUrl: String
)

fun GitHubRepo.toDomainModel(): DomainGitHubRepo = DomainGitHubRepo(
    name = this.fullName,
    description = this.description,
    url = this.htmlUrl
)

fun List<GitHubRepo>.toDomainModel(): List<DomainGitHubRepo> = this.map { it.toDomainModel() }
