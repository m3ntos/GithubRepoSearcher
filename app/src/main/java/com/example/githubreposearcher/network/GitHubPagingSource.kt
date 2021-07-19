package com.example.githubreposearcher.network

import androidx.paging.PagingState
import androidx.paging.rxjava2.RxPagingSource
import com.example.githubreposearcher.network.model.GitHubRepo
import com.example.githubreposearcher.network.model.RepositoriesResponse
import io.reactivex.Single
import io.reactivex.schedulers.Schedulers
import javax.inject.Singleton

@Singleton
class GitHubPagingSource(
    private val networkApi: NetworkApi,
    private val searchQuery: String,
    private val resultsPerPage: Int,
) : RxPagingSource<Int, GitHubRepo>() {


    override fun loadSingle(params: LoadParams<Int>): Single<LoadResult<Int, GitHubRepo>> {
        val pageNumber = params.key ?: 1

        return networkApi.getRepositories(searchQuery, pageNumber, resultsPerPage)
            .subscribeOn(Schedulers.io())
            .map { response -> toLoadResult(response, pageNumber) }
            .onErrorReturn { error -> LoadResult.Error(error) }
    }

    private fun toLoadResult(response: RepositoriesResponse, pageNumber: Int): LoadResult<Int, GitHubRepo> {
        return LoadResult.Page(
            data = response.items,
            prevKey = null, // Only paging forward.
            nextKey = pageNumber + 1,
            itemsAfter = (response.totalCount - pageNumber * resultsPerPage).coerceAtLeast(0),
        )
    }

    override fun getRefreshKey(state: PagingState<Int, GitHubRepo>): Int? {
        return state.anchorPosition?.let { anchorPosition ->
            val anchorPage = state.closestPageToPosition(anchorPosition)
            anchorPage?.prevKey?.plus(1) ?: anchorPage?.nextKey?.minus(1)
        }
    }
}