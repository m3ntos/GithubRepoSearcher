package com.example.githubreposearcher.common

import com.example.githubreposearcher.domain.Result
import io.reactivex.Observable
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers

fun <T : Any> Observable<T>.toResult(): Observable<Result<T>> = this
    .map<Result<T>> { Result.Success(it) }
    .onErrorReturn { Result.Error(it) }
    .startWith(Result.Loading)

fun <T : Any> Observable<T>.applySchedulers(): Observable<T> = this
    .subscribeOn(Schedulers.io())
    .observeOn(AndroidSchedulers.mainThread())