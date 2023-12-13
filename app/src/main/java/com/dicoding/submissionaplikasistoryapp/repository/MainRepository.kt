package com.dicoding.submissionaplikasistoryapp.repository

import androidx.lifecycle.LiveData
import androidx.paging.ExperimentalPagingApi
import androidx.paging.Pager
import androidx.paging.PagingConfig
import androidx.paging.PagingData
import androidx.paging.liveData
import com.dicoding.submissionaplikasistoryapp.data.StoryPaging
import com.dicoding.submissionaplikasistoryapp.data.StoryRemote
import com.dicoding.submissionaplikasistoryapp.database.ListStory
import com.dicoding.submissionaplikasistoryapp.database.StoryDatabase
import com.dicoding.submissionaplikasistoryapp.service.MainService
import javax.inject.Inject

class MainRepository @Inject constructor(private val storyDatabase: StoryDatabase, private val mainService: MainService){
    fun getStory(): LiveData<PagingData<ListStory>> {
        @OptIn(ExperimentalPagingApi::class)
        return Pager(
            config = PagingConfig(
                pageSize = 5
            ),
            remoteMediator = StoryRemote(storyDatabase, mainService),
            pagingSourceFactory = {
                StoryPaging(mainService)
                storyDatabase.storyDao().getAllStory()
            }
        ).liveData
    }
}