package com.example.githubreposearcher.network

import com.example.githubreposearcher.common.loadTestResource
import com.example.githubreposearcher.network.model.GitHubRepo
import okhttp3.mockwebserver.MockResponse
import org.junit.Assert.assertEquals
import org.junit.Test

class GetRepositoriesTest : BaseNetworkApiTest() {


    @Test
    fun `when api call is successful then response should be success and response body should be parsed`() {
        server.enqueue(MockResponse().setBody(loadTestResource("repositoriesResponse.json")))

        val repositoriesResponse = api.getRepositories("query").blockingGet()

        val responseItem = GitHubRepo(
            fullName = "m3ntos/GithubRepoSearcher",
            description = null,
            htmlUrl = "https://github.com/m3ntos/GithubRepoSearcher"
        )

        assertEquals(false, repositoriesResponse.incompleteResults)
        assertEquals(1, repositoriesResponse.totalCount)
        assertEquals(responseItem, repositoriesResponse.items[0])
    }
}