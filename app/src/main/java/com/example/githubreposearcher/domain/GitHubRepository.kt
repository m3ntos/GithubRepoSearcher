package com.example.githubreposearcher.domain

import com.example.githubreposearcher.domain.model.GitHubRepo
import com.example.githubreposearcher.network.NetworkApi
import com.example.githubreposearcher.network.model.toDomainModel
import io.reactivex.Single
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepository @Inject constructor(private val networkApi: NetworkApi) {

    fun getGithubRepositories(query: String): Single<List<GitHubRepo>> {
        return networkApi.getRepositories(query).map { it.items.toDomainModel() }
    }
}