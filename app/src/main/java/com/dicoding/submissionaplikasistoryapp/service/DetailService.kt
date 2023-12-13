package com.dicoding.submissionaplikasistoryapp.service

import com.dicoding.submissionaplikasistoryapp.data.DetailStoryResponse
import retrofit2.http.GET
import retrofit2.http.Path

interface DetailService {
    @GET("stories/{id}")
    suspend fun detailStory(@Path("id")id:String): DetailStoryResponse
}