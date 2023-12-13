package com.dicoding.submissionaplikasistoryapp.location

import androidx.lifecycle.ViewModel
import com.dicoding.submissionaplikasistoryapp.data.GetStoryResponse
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MapsViewModel @Inject constructor(private val mapsRepository: MapsRepository): ViewModel() {
    sealed class MapsResult<out T : Any> {
        data class Success<out T : Any>(val data: T) : MapsResult<T>()
        data class Error(val message: String) : MapsResult<Nothing>()
    }

    suspend fun getLatLong() : MapsResult<GetStoryResponse>{
        return when(val result = mapsRepository.getStoryWithCoordinates()){
            is MapsRepository.MapsResponse.Success -> MapsResult.Success(result.data)
            is MapsRepository.MapsResponse.Error -> MapsResult.Error(result.message)
        }
    }
}