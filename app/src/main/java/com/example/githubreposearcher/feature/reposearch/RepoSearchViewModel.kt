package com.example.githubreposearcher.feature.reposearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubreposearcher.domain.GitHubRepository
import com.example.githubreposearcher.domain.Result
import com.example.githubreposearcher.domain.model.GitHubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import io.reactivex.subjects.PublishSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RepoSearchViewModel @Inject constructor(productsRepository: GitHubRepository) : ViewModel() {

    private val disposable = CompositeDisposable()

    private val searchQuery: BehaviorSubject<String> = BehaviorSubject.create()
    private val retry: PublishSubject<Unit> = PublishSubject.create()

    private val repositoriesLiveData = MutableLiveData<Result<List<GitHubRepo>>>()

    init {
        disposable += searchQuery
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { it.length > 3 }
            .doOnNext { repositoriesLiveData.postValue(Result.Loading) }
            .switchMapSingle { query -> productsRepository.getGithubRepositories(query) }
            .doOnError { repositoriesLiveData.postValue(Result.Error(it)) }
            .retryWhen { it.flatMap { retry } }
            .subscribeBy { repositoriesLiveData.postValue(Result.Success(it)) }
    }

    public override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun repositoriesLiveData(): LiveData<Result<List<GitHubRepo>>> = repositoriesLiveData

    fun onSearchQueryChanged(query: String) {
        searchQuery.onNext(query)
    }

    fun onRetryClick() {
        retry.onNext(Unit)
    }
}



