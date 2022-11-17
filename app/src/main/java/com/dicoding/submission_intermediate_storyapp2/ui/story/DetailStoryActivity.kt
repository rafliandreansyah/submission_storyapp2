package com.dicoding.submission_intermediate_storyapp2.ui.story

import android.content.Intent
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.MenuItem
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityDetailStoryBinding
import com.dicoding.submission_intermediate_storyapp2.ui.auth.LoginActivity
import com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel.StoryViewModel
import com.dicoding.submission_intermediate_storyapp2.util.Result
import com.dicoding.submission_intermediate_storyapp2.util.changeFormatDate
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
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
        getDetailStoryData()
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == android.R.id.home) {
            super.onBackPressed()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun getDetailStoryData() {
        storyViewModel.getDetailStory(id).observe(this){ detailStoryResponse ->
            when (detailStoryResponse) {
                is Result.Loading -> {
                    isLoading(true)
                }
                is Result.Success -> {
                    isLoading(false)
                    with(binding) {
                        Glide.with(this@DetailStoryActivity)
                            .load(detailStoryResponse.data?.story?.photoUrl)
                            .into(imgStory)
                        txtDate.text = "Date created: ${changeFormatDate(detailStoryResponse.data?.story?.createdAt as String)}"
                        txtCreatedBy.text = detailStoryResponse.data.story.name
                        txtDescription.text = detailStoryResponse.data.story.description
                    }
                }
                else -> {
                    isLoading(false)
                    if (detailStoryResponse.code == 401) {
                        PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply()
                        val intent = Intent(this, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                        startActivity(intent)
                        finish()
                    }
                    Toast.makeText(this@DetailStoryActivity, detailStoryResponse.message, Toast.LENGTH_SHORT).show()
                    finish()

                }
            }
        }
//        storyViewModel.detailStoryData.observe(this) { detailStoryResponse ->
//            if (detailStoryResponse != null) {
//                isLoading(false)
//                if (detailStoryResponse.error == false) {
//                    with(binding) {
//                        Glide.with(this@DetailStoryActivity)
//                            .load(detailStoryResponse.story?.photoUrl)
//                            .into(imgStory)
//                        txtDate.text = "Date created: ${changeFormatDate(detailStoryResponse.story?.createdAt as String)}"
//                        txtCreatedBy.text = detailStoryResponse.story.name
//                        txtDescription.text = detailStoryResponse.story.description
//                    }
//                } else {
//                    if (detailStoryResponse.message.equals("unauthorized")) {
//                        Toast.makeText(this@DetailStoryActivity, "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
//                        val intent = Intent(this@DetailStoryActivity, LoginActivity::class.java)
//                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
//                        startActivity(intent)
//                        finish()
//                    }
//                    else {
//                        Toast.makeText(this@DetailStoryActivity, detailStoryResponse.message, Toast.LENGTH_SHORT).show()
//                    }
//                }
//            }
//
//        }
    }

    private fun isLoading(isL: Boolean) {
        if (isL) {
            binding.rlLoading.visibility = View.VISIBLE
        } else {
            binding.rlLoading.visibility = View.GONE
        }
    }

}