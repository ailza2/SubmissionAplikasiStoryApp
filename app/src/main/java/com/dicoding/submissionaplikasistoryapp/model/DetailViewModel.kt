package com.dicoding.submissionaplikasistoryapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionaplikasistoryapp.data.DetailStoryResponse
import com.dicoding.submissionaplikasistoryapp.repository.DetailRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class DetailViewModel @Inject constructor(private val detailRepository: DetailRepository):ViewModel() {
    sealed class ResultDetail<out T : Any> {
        data class Success<out T : Any>(val data: T) : ResultDetail<T>()
        data class Error(val error: String) : ResultDetail<Nothing>()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    suspend fun detailStory(id: String): ResultDetail<DetailStoryResponse>? {
        try {
            return try {
                _isLoading.value = true
                val result = detailRepository.resultDetail(id)
                _isLoading.value = false
                when (result) {
                    is DetailRepository.ResultDetail.Success -> ResultDetail.Success(result.data)
                    is DetailRepository.ResultDetail.Error -> ResultDetail.Error(result.error)
                }
            } catch (e: Exception) {
                _isLoading.value = false
                null
            }
        } catch (e: Exception) {
            _isLoading.value = false
            return ResultDetail.Error(e.toString())
        }
    }
}