package com.example.githubreposearcher.feature.reposearch

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.rxjava2.cachedIn
import com.example.githubreposearcher.domain.GitHubRepository
import com.example.githubreposearcher.domain.model.GitHubRepo
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign
import io.reactivex.rxkotlin.subscribeBy
import io.reactivex.subjects.BehaviorSubject
import java.util.concurrent.TimeUnit
import javax.inject.Inject

@HiltViewModel
class RepoSearchViewModel @Inject constructor(productsRepository: GitHubRepository) : ViewModel() {

    private val disposable = CompositeDisposable()
    private val searchQuery: BehaviorSubject<String> = BehaviorSubject.create()
    private val repositoriesLiveData = MutableLiveData<PagingData<GitHubRepo>>()

    init {
        disposable += searchQuery
            .debounce(500, TimeUnit.MILLISECONDS)
            .distinctUntilChanged()
            .filter { it.length > 3 }
            .switchMap { query -> productsRepository.getGithubRepositoriesPaged(query).cachedIn(viewModelScope) }
            .subscribeBy { repositoriesLiveData.postValue(it) }
    }

    public override fun onCleared() {
        super.onCleared()
        disposable.clear()
    }

    fun repositoriesLiveData(): LiveData<PagingData<GitHubRepo>> = repositoriesLiveData

    fun onSearchQueryChanged(query: String) {
        searchQuery.onNext(query)
    }
}



