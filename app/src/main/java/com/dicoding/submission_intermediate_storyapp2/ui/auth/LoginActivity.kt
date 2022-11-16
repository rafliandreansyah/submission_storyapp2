package com.dicoding.submission_intermediate_storyapp2.ui.auth

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.preference.PreferenceManager
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submission_intermediate_storyapp2.R
import com.dicoding.submission_intermediate_storyapp2.constant.PREF_TOKEN
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityLoginBinding
import com.dicoding.submission_intermediate_storyapp2.ui.auth.viewmodel.AuthViewModel
import com.dicoding.submission_intermediate_storyapp2.ui.story.ListStoryActivity
import com.dicoding.submission_intermediate_storyapp2.util.Result
import dagger.hilt.android.AndroidEntryPoint


@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        hideActionBar()
        isLoading(false)

        playAnimation()

        binding.edLoginEmail.addTextChangedListener(watcher())
        binding.edLoginPassword.addTextChangedListener(watcher())

        binding.txtRegister.setOnClickListener {
            startActivity(Intent(this, RegisterActivity::class.java))
        }

        binding.btnSignIn.setOnClickListener{
            login()
        }

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

    private fun playAnimation() {
        with(binding) {
            val titleLogin = ObjectAnimator.ofFloat(txtTitleLogin, View.ALPHA, 1f).setDuration(500)
            val titleEmail = ObjectAnimator.ofFloat(txtTitleEmail, View.ALPHA, 1f).setDuration(500)
            val edEmail = ObjectAnimator.ofFloat(edLoginEmail, View.ALPHA, 1f).setDuration(500)
            val titlePass = ObjectAnimator.ofFloat(txtTitlePassword, View.ALPHA, 1f).setDuration(500)
            val edPass = ObjectAnimator.ofFloat(edLoginPassword, View.ALPHA, 1f).setDuration(500)
            val btnSignIn = ObjectAnimator.ofFloat(btnSignIn, View.ALPHA, 1f).setDuration(500)
            val titleAtau = ObjectAnimator.ofFloat(txtAtau, View.ALPHA, 1f).setDuration(500)
            val titleRegister = ObjectAnimator.ofFloat(txtRegister, View.ALPHA, 1f).setDuration(500)

            AnimatorSet().apply {
                playSequentially(titleLogin, titleEmail, edEmail, titlePass, edPass, btnSignIn, titleAtau, titleRegister)
                start()
            }
        }
    }

    @Suppress("DEPRECATION")
    private fun login() {
        val email = binding.edLoginEmail.text.toString().trim()
        val password = binding.edLoginPassword.text.toString().trim()

        authViewModel.login(email, password).observe(this) { loginResult ->

            when(loginResult) {
                is Result.Loading -> {
                    isLoading(true)
                }
                is Result.Success -> {
                    isLoading(false)
                    val token = loginResult.data?.loginResult?.token
                    if (token.isNullOrBlank()) {
                        Toast.makeText(this@LoginActivity, resources.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()
                        return@observe
                    }
                    PreferenceManager.getDefaultSharedPreferences(this@LoginActivity)
                        .edit()
                        .putString(PREF_TOKEN, token)
                        .apply()
                    val intent = Intent(this@LoginActivity, ListStoryActivity::class.java)
                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                    finish()
                }
                else -> {
                    isLoading(false)

                    if (loginResult.message.equals("User not found")) {
                        binding.edLoginEmail.error = loginResult.message

                    } else if (loginResult.message.equals("Invalid password")) {
                        binding.edLoginPassword.error = loginResult.message
                    }
                    Toast.makeText(this@LoginActivity, loginResult.message ?: resources.getString(R.string.login_failed), Toast.LENGTH_SHORT).show()

                }
            }

        }
    }

    private fun watcher() : TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.btnSignIn.isEnabled =
                    binding.edLoginEmail.text.toString().trim().isNotEmpty() &&
                            binding.edLoginEmail.error == null &&
                            binding.edLoginPassword.text.toString().trim().isNotEmpty() &&
                            binding.edLoginPassword.error == null
            }

        }
    }

    private fun isLoading(isL: Boolean) {
        binding.btnSignIn.isEnabled = !isL
        if (isL) {
            binding.rlLoading.visibility = View.VISIBLE
        } else {
            binding.rlLoading.visibility = View.GONE
        }
    }
}