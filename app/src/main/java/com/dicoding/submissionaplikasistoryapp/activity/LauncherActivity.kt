package com.dicoding.submissionaplikasistoryapp.activity

import android.animation.AnimatorSet
import android.animation.ObjectAnimator
import android.animation.PropertyValuesHolder
import android.content.Intent
import android.os.Bundle
import android.view.animation.AccelerateDecelerateInterpolator
import androidx.appcompat.app.AppCompatActivity
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityLauncherBinding
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class LauncherActivity : AppCompatActivity() {

    private lateinit var binding: ActivityLauncherBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLauncherBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.hide()

        buttonLauncherListener()
        launcherAnimated()
    }

    private fun buttonLauncherListener() {
        binding.buttonLauncher.setOnClickListener {
            startActivity(Intent(this@LauncherActivity, LoginActivity::class.java))
        }
    }

    private fun launcherAnimated() {
        val imageObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            binding.imageViewLaucher,
            PropertyValuesHolder.ofFloat("scaleX", 0.5f, 1f),
            PropertyValuesHolder.ofFloat("scaleY", 0.5f, 1f),
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("translationY", 0f, -30f, 30f, -30f, 0f)
        ).apply {
            duration = 2000
            interpolator = AccelerateDecelerateInterpolator()
        }

        val text1ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            binding.textViewLauncher,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("translationY", 100f, 0f)
        ).apply {
            startDelay = 300
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val text2ObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            binding.textViewLauncher2,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("translationY", 100f, 0f)
        ).apply {
            startDelay = 700
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        val buttonObjectAnimator = ObjectAnimator.ofPropertyValuesHolder(
            binding.buttonLauncher,
            PropertyValuesHolder.ofFloat("alpha", 0f, 1f),
            PropertyValuesHolder.ofFloat("translationY", 100f, 0f)
        ).apply {
            startDelay = 1100
            duration = 500
            interpolator = AccelerateDecelerateInterpolator()
        }

        binding.apply {
            textViewLauncher.alpha = 0f
            textViewLauncher2.alpha = 0f
            buttonLauncher.alpha = 0f
        }

        AnimatorSet().apply {
            play(imageObjectAnimator).before(text1ObjectAnimator)
            play(text1ObjectAnimator).before(text2ObjectAnimator)
            play(text2ObjectAnimator).before(buttonObjectAnimator)
            start()
        }
    }
}