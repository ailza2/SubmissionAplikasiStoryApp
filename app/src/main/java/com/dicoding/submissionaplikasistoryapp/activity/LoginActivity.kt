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
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityLoginBinding
import com.dicoding.submissionaplikasistoryapp.model.LoginViewModel
import com.dicoding.submissionaplikasistoryapp.model.UserModel
import com.dicoding.submissionaplikasistoryapp.service.SettingPreferences
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class LoginActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLoginBinding
    private val viewModel by viewModels<LoginViewModel>()

    @Inject
    lateinit var settingPreferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        binding.hyperlinkRegisterButton.setOnClickListener {
            startActivity(Intent(this@LoginActivity, RegisterActivity::class.java))
        }

        binding.emailEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.emailEditText.error == null) {
                    isEnable()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.passwordEditText.addTextChangedListener(object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.passwordEditText.error == null) {
                    isEnable()
                }
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.loginButton.setOnClickListener {
            val email = binding.emailEditText.text.toString()
            val password = binding.passwordEditText.text.toString()
            lifecycleScope.launch {
                when (val result = viewModel.loginUser(email, password)) {
                    is LoginViewModel.ResultLogin.Success -> {
                        val userModel = UserModel(
                            result.data.loginResult.userId,
                            result.data.loginResult.name,
                            result.data.loginResult.token
                        )
                        settingPreferences.saveUser(userModel)
                        Toast.makeText(this@LoginActivity, "Welcome ${userModel.name}", Toast.LENGTH_SHORT).show()
                        startActivity(Intent(this@LoginActivity, MainActivity::class.java))
                        finish()
                    }

                    is LoginViewModel.ResultLogin.Error -> {
                        Toast.makeText(this@LoginActivity, result.error, Toast.LENGTH_SHORT).show()
                    }
                }
            }
            viewModel.showLoading.observe(this@LoginActivity) {
                showLoading(it)
            }
        }

        enableButton()
        animatedPlay()
    }

    private fun enableButton() {
        binding.loginButton.isEnabled = false
    }

    private fun animatedPlay() {
        val imageAnimator =
            ObjectAnimator.ofFloat(binding.imageViewAnimated, View.TRANSLATION_X, -45f, 45f).apply {
                duration = 6000
                repeatCount = ObjectAnimator.INFINITE
                repeatMode = ObjectAnimator.REVERSE
            }
        imageAnimator.start()

        val fadeInDuration = 500L
        val labelAnimator = ObjectAnimator.ofFloat(binding.greetingViewLabel, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val appViewAnimator =
            ObjectAnimator.ofFloat(binding.appViewLabel, View.ALPHA, 1f).setDuration(fadeInDuration)
        val loginAnimator = ObjectAnimator.ofFloat(binding.loginAuthenticationLabel, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val emailAnimator = ObjectAnimator.ofFloat(binding.emailEditText, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val passwordAnimator = ObjectAnimator.ofFloat(binding.passwordEditText, View.ALPHA, 1f)
            .setDuration(fadeInDuration)
        val buttonAnimator =
            ObjectAnimator.ofFloat(binding.loginButton, View.ALPHA, 1f).setDuration(fadeInDuration)
        val layoutAnimator =
            ObjectAnimator.ofFloat(binding.linearLayoutLoginFragment, View.ALPHA, 1f)
                .setDuration(fadeInDuration)

        val togetherAnimator = AnimatorSet().apply {
            playTogether(buttonAnimator, layoutAnimator)
        }

        AnimatorSet().apply {
            playSequentially(
                labelAnimator,
                appViewAnimator,
                loginAnimator,
                emailAnimator,
                passwordAnimator,
                togetherAnimator
            )
            start()
        }
    }

    private fun isEnable() {
        val email = binding.emailEditText.text
        val password = binding.passwordEditText.text

        binding.loginButton.isEnabled = !email.isNullOrEmpty() && !password.isNullOrEmpty()
    }

    private fun showLoading(it: Boolean) {
        binding.progressBar.visibility = if (it) View.VISIBLE else View.GONE
        binding.loginButton.isEnabled = !it
    }
}