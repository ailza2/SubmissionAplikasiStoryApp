package com.dicoding.submissionaplikasistoryapp.repository

import com.dicoding.submissionaplikasistoryapp.service.NewStoryService
import okhttp3.MultipartBody
import okhttp3.RequestBody
import javax.inject.Inject

class NewStoryRepository @Inject constructor(private val newStoryService: NewStoryService) {
    sealed class ResultNewStory<out T : Any>{
        data class Success<out T : Any>(val data: T) : ResultNewStory<T>()
        data class Error(val message: String) : ResultNewStory<Nothing>()
    }

    suspend fun newStory(description : RequestBody, file : MultipartBody.Part) : ResultNewStory<NewStoryResponse>{
        return try{
            val response = newStoryService.newStory(description, file)
            ResultNewStory.Success(response)
        }
        catch (e: Exception){
            ResultNewStory.Error(e.toString())
        }
    }
}