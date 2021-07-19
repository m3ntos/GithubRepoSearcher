package com.example.githubreposearcher.domain

sealed class Result<out T : Any> {
    object Loading : Result<Nothing>()
    data class Success<out T : Any>(val data: T) : Result<T>()
    data class Error(val exception: Throwable) : Result<Nothing>()
}