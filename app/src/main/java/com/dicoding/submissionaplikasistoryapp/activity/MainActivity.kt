package com.dicoding.submissionaplikasistoryapp.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.provider.Settings
import android.util.Log
import android.view.Menu
import androidx.activity.viewModels
import androidx.recyclerview.widget.LinearLayoutManager
import com.dicoding.submissionaplikasistoryapp.service.MainAdapter
import com.dicoding.submissionaplikasistoryapp.R
import com.dicoding.submissionaplikasistoryapp.data.LoadingAdapter
import com.dicoding.submissionaplikasistoryapp.database.ListStory
import com.dicoding.submissionaplikasistoryapp.databinding.ActivityMainBinding
import com.dicoding.submissionaplikasistoryapp.location.MapsActivity
import com.dicoding.submissionaplikasistoryapp.model.MainViewModel
import com.dicoding.submissionaplikasistoryapp.service.SettingPreferences
import dagger.hilt.android.AndroidEntryPoint
import javax.inject.Inject

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding
    private val viewModel by viewModels<MainViewModel>()
    private lateinit var adapter: MainAdapter

    @Inject
    lateinit var settingPreferences: SettingPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        supportActionBar?.show()
        supportActionBar?.title = getString(R.string.UI_action_bar)

        binding.fabAddNewStory.setOnClickListener {
            startActivity(Intent(this, NewStoryActivity::class.java))
        }

        binding.rvStory.layoutManager = LinearLayoutManager(this)

        setViewData()
        setActionBarTitle()
    }

    override fun onResume() {
        super.onResume()
        adapter.refresh()
    }


    private fun setActionBarTitle() {
        val userModel = settingPreferences.getUser()
        val username = userModel?.name

        supportActionBar?.apply {
            title = getString(R.string.label_greeting_user, username)
            setDisplayShowHomeEnabled(true)
        }
    }

    private fun setViewData() {
        adapter = MainAdapter()
        binding.rvStory.adapter = adapter.withLoadStateFooter(
            footer = LoadingAdapter { adapter.retry() }
        )

        viewModel.story.observe(this) {
            adapter.submitData(lifecycle, it)
            Log.d("mytag", "after submit data")
        }

        adapter.setOnItemClickListener(object : MainAdapter.OnItemClickListener {
            override fun onItemClick(story: ListStory?) {
                val adapterToDetail = Intent(this@MainActivity, DetailActivity::class.java)
                adapterToDetail.putExtra(ID, story?.id)
                startActivity(adapterToDetail)
            }
        })
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        val inflater = menuInflater
        inflater.inflate(R.menu.main_menu, menu)

        val logoutMenu = menu?.findItem(R.id.logout)
        logoutMenu?.setOnMenuItemClickListener {
            settingPreferences.clearUser()
            startActivity(Intent(this, LauncherActivity::class.java))
            finish()
            true
        }
        val mapsMenu = menu?.findItem(R.id.button_maps)
        mapsMenu?.setOnMenuItemClickListener {
            startActivity(Intent(this, MapsActivity::class.java))
            true
        }
        val settingMenu = menu?.findItem(R.id.button_setting)
        settingMenu?.setOnMenuItemClickListener {
            startActivity(Intent(Settings.ACTION_LOCALE_SETTINGS))
            true
        }
        return true
    }

    companion object {
        private const val ID = "id"
    }
}