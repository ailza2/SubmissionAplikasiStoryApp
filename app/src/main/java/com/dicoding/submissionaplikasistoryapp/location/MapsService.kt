package com.dicoding.submissionaplikasistoryapp.location

import com.dicoding.submissionaplikasistoryapp.data.GetStoryResponse
import retrofit2.http.GET
import retrofit2.http.Query

interface MapsService {
    @GET("stories")
    suspend fun fetchStoryWithCoordinates(@Query("location")location : String) : GetStoryResponse
}