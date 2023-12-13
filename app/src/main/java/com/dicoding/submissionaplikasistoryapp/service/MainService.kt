package com.dicoding.submissionaplikasistoryapp.service

import com.dicoding.submissionaplikasistoryapp.data.GetStoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MainService {
    @GET("stories")
    suspend fun fetchStory(
        @Query("page") page: Int,
        @Query("size") size: Int
    ): GetStoryResponse
}