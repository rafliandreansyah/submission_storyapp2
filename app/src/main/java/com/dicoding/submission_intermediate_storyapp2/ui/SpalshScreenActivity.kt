package com.dicoding.submission_intermediate_storyapp2.ui

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.preference.PreferenceManager
import android.view.WindowInsets
import android.view.WindowManager
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.constant.PREF_TOKEN
import com.dicoding.submission_intermediate_storyapp2.ui.auth.LoginActivity
import com.dicoding.submission_intermediate_storyapp2.ui.story.ListStoryActivity


class SpalshScreenActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        hideActionBar()

        Handler().postDelayed(Runnable
        // Using handler with postDelayed called runnable run method
        {

            val token = PreferenceManager.getDefaultSharedPreferences(this@SpalshScreenActivity).getString(PREF_TOKEN, "")
            if (token != null && token.isNotEmpty()){
                val i = Intent(this@SpalshScreenActivity, ListStoryActivity::class.java)
                startActivity(i)
                finish()
            } else {
                val i = Intent(this@SpalshScreenActivity, LoginActivity::class.java)
                startActivity(i)
                finish()
            }

        }, 3 * 1000)
    }

    private fun hideActionBar() {
        @Suppress("DEPRECATION")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            window.insetsController?.hide(WindowInsets.Type.statusBars())
        } else {
            window.setFlags(
                WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN
            )
        }
        supportActionBar?.hide()
    }
}