package com.example.githubreposearcher.network

import com.example.githubreposearcher.di.NetworkModule
import okhttp3.mockwebserver.MockWebServer
import org.junit.After
import org.junit.Before

open class BaseNetworkApiTest {

    protected lateinit var server: MockWebServer
    protected lateinit var api: NetworkApi

    @Before
    fun before() {
        server = MockWebServer()
        api = getNetworkApi(server.url("/").toString())
    }

    @After
    fun after() {
        server.shutdown()
    }

    private fun getNetworkApi(baseUrl: String): NetworkApi {
        val json = NetworkModule.provideJsonConfig()
        val okhttp = NetworkModule.provideOkHttpClient()
        val retrofit = NetworkModule.provideRetrofit(okhttp, json, baseUrl)
        return NetworkModule.provideNetworkApi(retrofit)
    }
}