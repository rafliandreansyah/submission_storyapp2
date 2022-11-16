package com.dicoding.submission_intermediate_storyapp2.ui.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.paging.PagingDataAdapter
import androidx.recyclerview.widget.DiffUtil
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.databinding.ItemStoryBinding
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.util.changeFormatDate
import javax.inject.Inject

class StoryAdapter @Inject constructor(): PagingDataAdapter<Story, StoryAdapter.StoryViewHolder>(DIFF_CALLBACK) {

    companion object{
        private val DIFF_CALLBACK = object : DiffUtil.ItemCallback<Story>(){
            override fun areItemsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem == newItem
            }

            override fun areContentsTheSame(oldItem: Story, newItem: Story): Boolean {
                return oldItem.id == newItem.id
            }

        }
    }

    private lateinit var onItemClickListener: OnItemClickListener

    fun setOnItemClicked(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bindData(getItem(position) ?: Story())
    }

    inner class StoryViewHolder(private val view: ItemStoryBinding): RecyclerView.ViewHolder(view.root){
        fun bindData(story: Story) {
            with(view) {
                txtDate.text = story.createdAt?.let { changeFormatDate(it) }
                txtDescription.text = story.description
                txtCreatedBy.text = story.name

                Glide.with(this.root)
                    .load(story.photoUrl)
                    .placeholder(R.drawable.img_placeholder)
                    .error(R.drawable.image_error)
                    .transition(DrawableTransitionOptions.withCrossFade())
                    .into(imgStory)

                cvItemStory.setOnClickListener {
                    onItemClickListener.onItemClicked(story.id as String)
                }
            }
        }
    }

    interface OnItemClickListener {
        fun onItemClicked(id: String)
    }

}