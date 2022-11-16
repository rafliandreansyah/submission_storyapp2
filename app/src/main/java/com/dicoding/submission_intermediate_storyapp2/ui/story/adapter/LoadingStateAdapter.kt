package com.dicoding.submission_intermediate_storyapp2.ui.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.core.view.isVisible
import androidx.paging.LoadState
import androidx.paging.LoadStateAdapter
import androidx.recyclerview.widget.RecyclerView
import com.dicoding.submission_intermediate_storyapp2.databinding.ItemLoadingBinding

class LoadingStateAdapter(private val retry: () -> Unit): LoadStateAdapter<LoadingStateAdapter.LoadingStateViewHolder>() {


    inner class LoadingStateViewHolder(private val binding: ItemLoadingBinding, retry: () -> Unit):
        RecyclerView.ViewHolder(binding.root) {

        init {
            binding.retryButton.setOnClickListener{
                retry.invoke()
            }
        }

        fun bindData(loadState: LoadState) {
            if (loadState is LoadState.Error) {
                binding.errorMsg.text = "Error get data please check your internet!"
            }
            binding.progressBar.isVisible = loadState is LoadState.Loading
            binding.retryButton.isVisible = loadState is LoadState.Error
            binding.errorMsg.isVisible = loadState is LoadState.Error
        }
    }

    override fun onBindViewHolder(holder: LoadingStateViewHolder, loadState: LoadState) {
        holder.bindData(loadState)
    }

    override fun onCreateViewHolder(
        parent: ViewGroup,
        loadState: LoadState
    ): LoadingStateViewHolder {
        val view = ItemLoadingBinding.inflate(LayoutInflater.from(parent.context), parent, false)
        return LoadingStateViewHolder(view, retry)
    }

}