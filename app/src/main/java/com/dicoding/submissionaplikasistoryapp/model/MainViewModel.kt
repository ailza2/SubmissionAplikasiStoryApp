package com.dicoding.submissionaplikasistoryapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.paging.PagingData
import androidx.paging.cachedIn
import com.dicoding.submissionaplikasistoryapp.database.ListStory
import com.dicoding.submissionaplikasistoryapp.repository.MainRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainViewModel @Inject constructor(private val mainRepository: MainRepository):ViewModel() {
    val story: LiveData<PagingData<ListStory>> =
        mainRepository.getStory().cachedIn(viewModelScope)
}