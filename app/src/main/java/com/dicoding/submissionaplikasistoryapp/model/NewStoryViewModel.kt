package com.dicoding.submissionaplikasistoryapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionaplikasistoryapp.repository.NewStoryRepository
import com.dicoding.submissionaplikasistoryapp.repository.NewStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

@HiltViewModel
class NewStoryViewModel @Inject constructor(private val newStoryRepository: NewStoryRepository) : ViewModel(){
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    sealed class Result <out T : Any>{
        data class Success<out T : Any>(val data : T) : Result<T>()
        data class Error(val message: String) : Result<Nothing>()
    }

    suspend fun newStory(description: RequestBody, file: MultipartBody.Part): Result<NewStoryResponse> {
        _isLoading.value = true
        return try {
            val response = newStoryRepository.newStory(description, file)
            _isLoading.value = false
            when (response) {
                is NewStoryRepository.ResultNewStory.Success -> Result.Success(response.data)
                is NewStoryRepository.ResultNewStory.Error -> Result.Error(response.message)
            }
        } catch (e: Exception) {
            _isLoading.value = false
            Result.Error(e.toString())
        }
    }
}