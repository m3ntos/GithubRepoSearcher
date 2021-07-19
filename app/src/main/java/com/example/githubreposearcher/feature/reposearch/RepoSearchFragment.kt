package com.example.githubreposearcher.feature.reposearch

import android.net.Uri
import android.os.Bundle
import android.view.View
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.view.isVisible
import androidx.core.widget.doOnTextChanged
import androidx.fragment.app.Fragment
import androidx.fragment.app.viewModels
import androidx.paging.CombinedLoadStates
import androidx.paging.LoadState
import com.example.githubreposearcher.R
import com.example.githubreposearcher.databinding.FragmentRepoSearchBinding
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
            addLoadStateListener { loadStates -> onLoadStatesChange(binding, loadStates) }
            errorView.btnRetry.setOnClickListener { this.retry() }
            viewModel.repositoriesLiveData().observe(viewLifecycleOwner) { pagingData ->
                submitData(viewLifecycleOwner.lifecycle, pagingData)
            }
        }
    }

    private fun onLoadStatesChange(binding: FragmentRepoSearchBinding, loadStates: CombinedLoadStates) = with(binding) {
        val stateViews = listOf(loadingView.root, errorView.root, emptyView.root, searchResultsRv)
        stateViews.forEach { it.isVisible = false }

        when (loadStates.refresh) {
            LoadState.Loading -> {
                loadingView.root.isVisible = true
            }
            is LoadState.Error -> {
                errorView.root.isVisible = true
                errorView.tvDescription.text = (loadStates.refresh as LoadState.Error).error.message
            }
            is LoadState.NotLoading -> {
                val isEmptyList = loadStates.append.endOfPaginationReached && searchResultsRv.adapter?.itemCount == 0

                if (isEmptyList) emptyView.root.isVisible = true
                else searchResultsRv.isVisible = true
            }
        }
    }

    private fun openBrowserTab(url: String) {
        CustomTabsIntent.Builder().build().launchUrl(requireContext(), Uri.parse(url))
    }
}