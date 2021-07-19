package com.example.githubreposearcher.feature.reposearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.example.githubreposearcher.R
import com.example.githubreposearcher.databinding.ItemRepoSearchBinding
import com.example.githubreposearcher.domain.model.GitHubRepo
import com.example.githubreposearcher.feature.reposearch.RepoSearchAdapter.ViewHolder

class RepoSearchAdapter : PagingDataAdapter<GitHubRepo, ViewHolder>(DiffCallBack) {

    private var onItemClickListener: ((GitHubRepo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(getItem(position))

    fun setOnItemClickListener(listener: ((GitHubRepo) -> Unit)) {
        onItemClickListener = listener
    }

    inner class ViewHolder(val binding: ItemRepoSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GitHubRepo?) = with(binding) {
            // TODO make nice shimmer view
            tvTitle.text = item?.name ?: itemView.context.getString(R.string.loadingLabel)
            tvDescription.text = item?.description ?: "..."

            if (item != null) root.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }

    private object DiffCallBack : DiffUtil.ItemCallback<GitHubRepo>() {
        override fun areItemsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean = oldItem.name == newItem.name
        override fun areContentsTheSame(oldItem: GitHubRepo, newItem: GitHubRepo): Boolean = oldItem == newItem
    }
}