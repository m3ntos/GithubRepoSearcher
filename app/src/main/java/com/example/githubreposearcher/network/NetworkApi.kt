package com.example.githubreposearcher.network

import com.example.githubreposearcher.network.model.RepositoriesResponse
import io.reactivex.Single
import retrofit2.http.GET
import retrofit2.http.Query


interface NetworkApi {

    @GET("repositories")
    fun getRepositories(
        @Query("q") searchQuery: String,
        @Query("page") page: Int,
        @Query("per_page") perPage: Int
    ): Single<RepositoriesResponse>
}
