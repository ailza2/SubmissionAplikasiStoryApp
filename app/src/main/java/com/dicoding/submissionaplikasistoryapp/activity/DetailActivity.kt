package com.dicoding.submissionaplikasistoryapp.activity

import android.os.Bundle
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.lifecycleScope
import com.bumptech.glide.Glide
import com.dicoding.submissionaplikasistoryapp.data.Story
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityDetailBinding
import com.dicoding.submissionaplikasistoryapp.model.DetailViewModel
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch

@AndroidEntryPoint
class DetailActivity : AppCompatActivity() {

    private lateinit var binding: ActivityDetailBinding
    private val viewModel by viewModels<DetailViewModel>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val id = intent.getStringExtra("id")
        if (!id.isNullOrEmpty()) {
            lifecycleScope.launch {
                when (val result = viewModel.detailStory(id)) {
                    is DetailViewModel.ResultDetail.Success -> {
                        val story = result.data
                        setData(story.story)
                    }

                    is DetailViewModel.ResultDetail.Error -> {
                        showErrorDialog(result.error)
                    }

                    else -> {}
                }
            }
        }
        viewModel.isLoading.observe(this) {
            showLoading(it)
        }
    }

    private fun setData(it: Story) {
        Glide.with(this)
            .load(it.photoUrl)
            .into(binding.ivDetailStory)

        binding.tvTitleStory.text = it.name
        binding.tvStoryDescription.text = it.description
    }

    private fun showErrorDialog(errorMessage: String) {
        val builder = AlertDialog.Builder(this)
        builder.setTitle("Error")
            .setMessage(errorMessage)
            .setPositiveButton("OK") { dialog, _ ->
                dialog.dismiss()
            }
            .setCancelable(false)
            .create()
            .show()
    }


    private fun showLoading(state: Boolean) {
        binding.progressBar.visibility = if (state) View.VISIBLE else View.GONE
    }
}
