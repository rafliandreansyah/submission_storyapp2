package com.dicoding.submission_intermediate_storyapp2.ui.auth

import android.os.Build
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.WindowInsets
import android.view.WindowManager
import android.widget.Toast
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submission_intermediate_storyapp2.databinding.ActivityRegisterBinding
import com.dicoding.submission_intermediate_storyapp2.ui.auth.viewmodel.AuthViewModel
import com.dicoding.submission_intermediate_storyapp2.util.Result
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val authViewModel: AuthViewModel by viewModels()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        hideActionBar()
        isLoading(false)

        binding.edRegisterName.addTextChangedListener(watcher())
        binding.edRegisterEmail.addTextChangedListener(watcher())
        binding.edRegisterPassword.addTextChangedListener(watcher())
        binding.edRegisterPasswordConfirm.addTextChangedListener(watcher())

        binding.btnRegister.setOnClickListener {
            register()
        }

        binding.txtLogin.setOnClickListener {
            super.onBackPressed()
        }
    }

    private fun register() {
        val name = binding.edRegisterName.text.toString().trim()
        val email = binding.edRegisterEmail.text.toString().trim()
        val password = binding.edRegisterPassword.text.toString().trim()
        val confirmPassword = binding.edRegisterPasswordConfirm.text.toString()

        if (password != confirmPassword) {
            binding.edRegisterPasswordConfirm.error = "Password tidak sama"
            Toast.makeText(this, "Password tidak sama", Toast.LENGTH_SHORT).show()
            return
        }

        authViewModel.register(name, email, password).observe(this) { registerResult ->

            when(registerResult) {
                is Result.Loading -> isLoading(true)
                is Result.Success -> {
                    isLoading(false)
                    Toast.makeText(this, "Success register", Toast.LENGTH_LONG).show()
                    finish()
                }
                else -> {
                    isLoading(false)
                    if (registerResult.message.equals("Email is already taken")) {
                        binding.edRegisterEmail.error = registerResult.message
                    }
                    Toast.makeText(this, registerResult.message ?: "error register", Toast.LENGTH_LONG).show()
                }
            }

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

    private fun watcher() : TextWatcher {
        return object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {}

            override fun afterTextChanged(s: Editable?) {
                binding.btnRegister.isEnabled =
                    binding.edRegisterEmail.text.toString().trim().isNotEmpty() &&
                            binding.edRegisterEmail.error == null &&
                            binding.edRegisterPassword.text.toString().trim().isNotEmpty() &&
                            binding.edRegisterPassword.error == null &&
                            binding.edRegisterPasswordConfirm.text.toString().trim().isNotEmpty() &&
                            binding.edRegisterName.text.toString().trim().isNotEmpty()
            }

        }
    }

    private fun isLoading(isL: Boolean) {
        binding.btnRegister.isEnabled = !isL
        if (isL) {
            binding.rlLoading.visibility = View.VISIBLE
        } else {
            binding.rlLoading.visibility = View.GONE
        }
    }

}