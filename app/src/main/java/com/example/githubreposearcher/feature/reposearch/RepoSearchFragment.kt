package com.example.githubreposearcher.feature.reposearch

import android.os.Bundle
import android.util.Log
import android.view.View
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.githubreposearcher.R
import com.example.githubreposearcher.common.applySchedulers
import com.example.githubreposearcher.databinding.FragmentRepoSearchBinding
import com.example.githubreposearcher.domain.Result
import com.example.githubreposearcher.domain.model.GitHubRepo
import dagger.hilt.android.AndroidEntryPoint
import io.reactivex.disposables.CompositeDisposable
import io.reactivex.rxkotlin.plusAssign

@AndroidEntryPoint
class RepoSearchFragment : Fragment(R.layout.fragment_repo_search) {

    private val viewModel: RepoSearchViewModel by viewModels()
    private val disposable = CompositeDisposable()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRepoSearchBinding.bind(view)

        setupSearchBar(binding)
        setupReposList(binding)
    }

    override fun onDestroyView() {
        super.onDestroyView()
        disposable.clear()
    }

    private fun setupSearchBar(binding: FragmentRepoSearchBinding) = with(binding) {
        searchInput.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }
    }

    private fun setupReposList(binding: FragmentRepoSearchBinding) = with(binding) {
        binding.searchResultsRv.adapter = RepoSearchAdapter()

        disposable += viewModel.repositoriesList
            .applySchedulers()
            .subscribe { renderState(binding, it) }
    }

    private fun renderState(binding: FragmentRepoSearchBinding, result: Result<List<GitHubRepo>>) = with(binding) {
        searchResultsGroup.isVisible = false
        when (result) {
            Result.Loading -> {
                loadingView.root.isVisible = true
            }
            is Result.Error -> {
                errorView.root.isVisible = true
                errorView.tvDescription.text = result.exception.message.toString()
            }
            is Result.Success -> {
                if (result.data.isEmpty()) emptyView.root.isVisible = true
                else searchResultsRv.isVisible = true

                (searchResultsRv.adapter as RepoSearchAdapter).setList(result.data)
            }
        }
    }
}