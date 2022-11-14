package com.dicoding.submission_intermediate_storyapp2.ui.story.adapter

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.bumptech.glide.load.resource.drawable.DrawableTransitionOptions
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.databinding.ItemStoryBinding
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.util.changeFormatDate

class StoryAdapter: RecyclerView.Adapter<StoryAdapter.StoryViewHolder>() {

    private var listStory: List<Story>? = null
    private lateinit var onItemClickListener: OnItemClickListener

    fun setData(listStory: List<Story>) {
        this.listStory = listStory
    }

    fun setOnItemClicked(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): StoryViewHolder {
        return StoryViewHolder(ItemStoryBinding.inflate(LayoutInflater.from(parent.context), parent, false))
    }

    override fun onBindViewHolder(holder: StoryViewHolder, position: Int) {
        holder.bindData(listStory?.get(position) ?: Story())
    }

    override fun getItemCount(): Int = if (listStory != null)  listStory!!.size else 0

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