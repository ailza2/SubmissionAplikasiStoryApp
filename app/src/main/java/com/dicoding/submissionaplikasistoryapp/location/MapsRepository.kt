package com.dicoding.submissionaplikasistoryapp.location

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionaplikasistoryapp.data.GetStoryResponse

class MapsRepository(private val mapsService: MapsService) {
    sealed class MapsResponse<out T : Any> {
        data class Success<out T : Any>(val data: T) : MapsResponse<T>()
        data class Error(val message: String) : MapsResponse<Nothing>()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean>
        get() = _isLoading

    suspend fun getStoryWithCoordinates(): MapsResponse<GetStoryResponse> {
        _isLoading.value = true
        return try {
            val data = "1"
            val response = mapsService.fetchStoryWithCoordinates(data)
            _isLoading.value = false
            MapsResponse.Success(response)
        } catch (e: Exception) {
            _isLoading.value = false
            MapsResponse.Error(e.message.toString())
        }
    }
}