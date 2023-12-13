package com.dicoding.submissionaplikasistoryapp.repository

import com.dicoding.submissionaplikasistoryapp.data.DetailStoryResponse
import com.dicoding.submissionaplikasistoryapp.service.DetailService
import java.lang.Exception
import javax.inject.Inject

class DetailRepository @Inject constructor(private val detailService: DetailService) {
    sealed class ResultDetail<out T : Any> {
        data class Success<out T : Any>(val data: T) : ResultDetail<T>()
        data class Error(val error: String) : ResultDetail<Nothing>()
    }

    suspend fun resultDetail(id: String ) :  ResultDetail<DetailStoryResponse>{
        return try {
            val result = detailService.detailStory(id)
            ResultDetail.Success(result)
        }catch (e: Exception){
            ResultDetail.Error(e.toString())
        }
    }
}