package com.example.githubreposearcher.feature.reposearch

import androidx.lifecycle.ViewModel
import com.example.githubreposearcher.common.toResult
import com.example.githubreposearcher.domain.GitHubRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import io.reactivex.subjects.BehaviorSubject
import javax.inject.Inject

@HiltViewModel
class RepoSearchViewModel @Inject constructor(productsRepository: GitHubRepository) : ViewModel() {

    private val searchQuery: BehaviorSubject<String> = BehaviorSubject.create()

    val repositoriesList = searchQuery
        .switchMapSingle { query -> productsRepository.getGithubRepositories(query) }
        .toResult()

    fun onSearchQueryChanged(query: String) {
        searchQuery.onNext(query)
    }
}



