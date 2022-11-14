package com.dicoding.submission_intermediate_storyapp2.ui.story

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.*
import android.widget.Toast
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityListStoryBinding
import com.dicoding.submission_intermediate_storyapp2.model.Story
import com.dicoding.submission_intermediate_storyapp2.ui.auth.LoginActivity
import com.dicoding.submission_intermediate_storyapp2.ui.story.adapter.StoryAdapter
import com.dicoding.submission_intermediate_storyapp2.ui.story.viewmodel.StoryViewModel

class ListStoryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityListStoryBinding
    private val storyViewModel: StoryViewModel by viewModels()
    private lateinit var storyAdapter: StoryAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityListStoryBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        supportActionBar?.title = "List Story"
        initRecycleview()

        isLoading(true)
        storyViewModel.getAllStory()
        setData()

        binding.swipeRefresh.setOnRefreshListener {
            binding.swipeRefresh.isRefreshing = false
            isLoading(true)
            storyViewModel.getAllStory()
        }

        binding.fabAddStory.setOnClickListener {
            startActivity(Intent(this, CreateStoryActivity::class.java))
        }

        storyAdapter.setOnItemClicked(object : StoryAdapter.OnItemClickListener{
            override fun onItemClicked(id: String) {
                val intent = Intent(this@ListStoryActivity, DetailStoryActivity::class.java)
                intent.putExtra(DetailStoryActivity.STORY_ID, id)
                startActivity(intent)
            }

        })
    }

    override fun onResume() {
        super.onResume()

        if (intent.extras != null){
            val isRestart = intent.getBooleanExtra("reload", false)
            if (isRestart) {
                isLoading(true)
                storyViewModel.getAllStory()
            }
        }
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        menuInflater.inflate(R.menu.menu_logout, menu)
        return super.onCreateOptionsMenu(menu)
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        if (item.itemId == R.id.logout) {
            PreferenceManager.getDefaultSharedPreferences(this).edit().clear().apply()
            val intent = Intent(this, LoginActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }
        return super.onOptionsItemSelected(item)
    }

    private fun setData(){
        storyViewModel.listStoryData.observe(this) { responseListStory ->
            isLoading(false)
            if (responseListStory != null) {
                if (!responseListStory.error) {
                    storyAdapter.setData(responseListStory.listStory as List<Story>)
                    storyAdapter.notifyDataSetChanged()
                }
                else {
                    if (responseListStory.message.equals("unauthorized")) {
                        Toast.makeText(this@ListStoryActivity, "Your token expired, please relogin!", Toast.LENGTH_SHORT).show()
                        val intent = Intent(this@ListStoryActivity, LoginActivity::class.java)
                        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                        startActivity(intent)
                        finish()
                    }
                    else {
                        Toast.makeText(this@ListStoryActivity, responseListStory.message, Toast.LENGTH_SHORT).show()
                    }
                }
            }
        }
    }

    private fun initRecycleview() {
        storyAdapter = StoryAdapter()
        with(binding) {
            rvListStory.layoutManager = LinearLayoutManager(this@ListStoryActivity)
            rvListStory.setHasFixedSize(true)
            rvListStory.adapter = storyAdapter
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