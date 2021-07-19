package com.example.githubreposearcher.feature.reposearch

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import com.example.githubreposearcher.R
import com.example.githubreposearcher.databinding.FragmentRepoSearchBinding
import com.example.githubreposearcher.domain.Result
import com.example.githubreposearcher.domain.model.GitHubRepo
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class RepoSearchFragment : Fragment(R.layout.fragment_repo_search) {

    private val viewModel: RepoSearchViewModel by viewModels()

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val binding = FragmentRepoSearchBinding.bind(view)
        setupViews(binding)
    }

    private fun setupViews(binding: FragmentRepoSearchBinding) = with(binding) {
        searchInput.editText?.doOnTextChanged { text, _, _, _ ->
            viewModel.onSearchQueryChanged(text?.toString() ?: "")
        }
        searchResultsRv.adapter = RepoSearchAdapter().apply {
            setOnItemClickListener { openBrowserTab(it.url) }
        }
        errorView.btnRetry.setOnClickListener { viewModel.onRetryClick() }

        viewModel.repositoriesLiveData().observe(viewLifecycleOwner) { renderState(binding, it) }
    }

    private fun renderState(binding: FragmentRepoSearchBinding, result: Result<List<GitHubRepo>>) = with(binding) {
        val stateViews = listOf(loadingView.root, errorView.root, emptyView.root, searchResultsRv)
        stateViews.forEach { it.isVisible = false }

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

    private fun openBrowserTab(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(requireContext(), Uri.parse(url))
    }
}