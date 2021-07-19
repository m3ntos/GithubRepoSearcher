package com.example.githubreposearcher.domain

import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.map
import androidx.paging.rxjava2.observable
import com.example.githubreposearcher.domain.model.GitHubRepo
import com.example.githubreposearcher.network.GitHubPagingSource
import com.example.githubreposearcher.network.NetworkApi
import com.example.githubreposearcher.network.model.toDomainModel
import io.reactivex.Observable
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class GitHubRepository @Inject constructor(private val networkApi: NetworkApi) {

    fun getGithubRepositoriesPaged(query: String): Observable<PagingData<GitHubRepo>> {
        val pagingConfig = PagingConfig(pageSize = PAGE_SIZE)
        val pagingSource = GitHubPagingSource(networkApi, searchQuery = query, resultsPerPage = PAGE_SIZE)
        val pager = Pager(config = pagingConfig, pagingSourceFactory = { pagingSource })

        return pager.observable.map { pagingData -> pagingData.map { it.toDomainModel() } }
    }

    companion object {
        private const val PAGE_SIZE = 30
    }
}