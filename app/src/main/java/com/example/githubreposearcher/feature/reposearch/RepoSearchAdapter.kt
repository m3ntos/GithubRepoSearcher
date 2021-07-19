package com.example.githubreposearcher.feature.reposearch

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.example.githubreposearcher.databinding.ItemRepoSearchBinding
import com.example.githubreposearcher.domain.model.GitHubRepo
import com.example.githubreposearcher.feature.reposearch.RepoSearchAdapter.ViewHolder

class RepoSearchAdapter : RecyclerView.Adapter<ViewHolder>() {

    private var items: List<GitHubRepo> = listOf()
    private var onItemClickListener: ((GitHubRepo) -> Unit)? = null

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val binding = ItemRepoSearchBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return ViewHolder(binding)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) = holder.bind(items[position])

    override fun getItemCount(): Int = items.size

    fun setOnItemClickListener(listener: ((GitHubRepo) -> Unit)) {
        onItemClickListener = listener
    }

    fun setList(items: List<GitHubRepo>) {
        this.items = items
        notifyDataSetChanged()
    }

    inner class ViewHolder(val binding: ItemRepoSearchBinding) :
        RecyclerView.ViewHolder(binding.root) {

        fun bind(item: GitHubRepo) = with(binding) {
            tvTitle.text = item.name
            tvDescription.text = item.description
            root.setOnClickListener { onItemClickListener?.invoke(item) }
        }
    }
}