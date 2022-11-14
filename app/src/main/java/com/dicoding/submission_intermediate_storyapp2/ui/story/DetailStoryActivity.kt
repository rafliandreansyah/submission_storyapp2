package com.dicoding.submission_intermediate_storyapp2.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import com.bumptech.glide.Glide
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityDetailStoryBinding
import com.dicoding.submission_intermediate_storyapp2.ui.auth.LoginActivity
import com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel.StoryViewModel
import com.dicoding.submission_intermediate_storyapp2.util.changeFormatDate

class DetailStoryActivity : AppCompatActivity() {
    companion object{
        const val STORY_ID = "STORY_ID"
    }
    private lateinit var binding: ActivityDetailStoryBinding
    private val storyViewModel: StoryViewModel by viewModels()

    private var id: String = "0"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailStoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        supportActionBar?.setDisplayHomeAsUpEnabled(true);
        supportActionBar?.title = "Detail Story"

        if (intent.extras != null) {
            id = intent.getStringExtra(STORY_ID).toString()
        }

        isLoading(true)
        storyViewModel.getDetailStory(id)
        setDetailStory()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setDetailStory() {
        storyViewModel.detailStoryData.observe(this) { detailStoryResponse ->
            if (detailStoryResponse != null) {
                isLoading(false)
                if (detailStoryResponse.error == false) {
                    with(binding) {
                        Glide.with(this@DetailStoryActivity)
                            .load(detailStoryResponse.story?.photoUrl)
                            .into(imgStory)
                        txtDate.text = "Date created: ${changeFormatDate(detailStoryResponse.story?.createdAt as String)}"
                        txtCreatedBy.text = detailStoryResponse.story.name
                        txtDescription.text = detailStoryResponse.story.description
                    }
                } else {
                    if (detailStoryResponse.message.equals("unauthorized")) {
                        Toast.makeText(this@DetailStoryActivity, "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@DetailStoryActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@DetailStoryActivity, detailStoryResponse.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }

        }
    }

    private fun isLoading(isL: Boolean) {
        if (isL) {
            binding.rlLoading.visibility = View.VISIBLE
        } else {
            binding.rlLoading.visibility = View.GONE
        }
    }

}