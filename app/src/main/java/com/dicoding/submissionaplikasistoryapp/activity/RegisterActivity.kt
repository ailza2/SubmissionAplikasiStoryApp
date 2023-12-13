package com.dicoding.submissionaplikasistoryapp.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import androidx.activity.viewModels
import androidx.lifecycle.lifecycleScope
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityRegisterBinding
import com.dicoding.submissionaplikasistoryapp.model.RegisterViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class RegisterActivity : AppCompatActivity() {

    private lateinit var binding: ActivityRegisterBinding
    private val viewModel by viewModels<RegisterViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRegisterBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.hyperlinkLoginButton.setOnClickListener {
            startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
        }

        binding.registerButton.setOnClickListener {
            val name = binding.nameEditText.text.toString()
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()

            lifecycleScope.launch {
                when (val response = viewModel.userRegister(name, email, password)) {
                    is RegisterViewModel.ResultRegister.Success -> {
                        val data = response.data
                        Toast.makeText(this@RegisterActivity, data.message, Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@RegisterActivity, LoginActivity::class.java))
                        finish()
                    }
                    is RegisterViewModel.ResultRegister.Error -> {
                        val error = response.error
                        Toast.makeText(this@RegisterActivity, error, Toast.LENGTH_SHORT).show()
                    }
                }
            }}
        viewModel.isLoading.observe(this) {
            isLoading(it)
        }

        enableRegisterButton()
        animatedPlay()
        enableButton()
    }

    private fun enableButton() {
        binding.registerButton.isEnabled = false
    }

    private fun enableRegisterButton() {
        binding.nameEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.nameEditText.error == null){
                    isEnable()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.nameEditText.error == null){
                    isEnable()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.passwordEditText.error == null){
                    isEnable()
                }
            }
            override fun afterTextChanged(p0: Editable?) {}
        })
    }

    private fun animatedPlay() {
        val imageAnimator = ObjectAnimator.ofFloat(binding.imageViewAnimated, View.TRANSLATION_X, -45f, 45f).apply {
            duration = 6000
            repeatCount = ObjectAnimator.INFINITE
            repeatMode = ObjectAnimator.REVERSE
        }
        imageAnimator.start()

        val fadeInDuration = 500L
        val labelAnimator = ObjectAnimator.ofFloat(binding.greetingViewLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val appViewAnimator = ObjectAnimator.ofFloat(binding.appViewLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val loginAnimator = ObjectAnimator.ofFloat(binding.loginAuthenticationLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val nameAnimator = ObjectAnimator.ofFloat(binding.nameEditText, View.ALPHA, 1f).setDuration(fadeInDuration)
        val emailAnimator = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f).setDuration(fadeInDuration)
        val passwordAnimator = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f).setDuration(fadeInDuration)
        val buttonAnimator = ObjectAnimator.ofFloat(binding.registerButton, View.ALPHA, 1f).setDuration(fadeInDuration)
        val layoutAnimator = ObjectAnimator.ofFloat(binding.linearLayoutLoginFragment, View.ALPHA, 1f).setDuration(fadeInDuration)

        val togetherAnimator = AnimatorSet().apply {
            playTogether(buttonAnimator, layoutAnimator)
        }

        AnimatorSet().apply {
            playSequentially(labelAnimator, appViewAnimator, loginAnimator, nameAnimator, emailAnimator, passwordAnimator, togetherAnimator)
            start()
        }
    }

    fun isEnable() {
        val name = binding.nameEditText.text
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text
        binding.registerButton.isEnabled =
            !name.isNullOrEmpty() && !email.isNullOrEmpty() && !password.isNullOrEmpty()
                    && binding.nameEditText.error == null && binding.emailEditText.error == null
                    && binding.passwordEditText.error == null

        if (!binding.registerButton.isEnabled) {
            binding.registerButton.alpha = 0.5f
        }
        else {
            binding.registerButton.alpha = 1f
        }
    }

    private fun isLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
    }
}