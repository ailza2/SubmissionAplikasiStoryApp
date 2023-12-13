package com.dicoding.submissionaplikasistoryapp.service

import com.dicoding.submissionaplikasistoryapp.repository.NewStoryResponse
import okhttp3.MultipartBody
import okhttp3.RequestBody
import retrofit2.http.Multipart
import retrofit2.http.POST
import retrofit2.http.Part

interface NewStoryService {
    @Multipart
    @POST("stories")
    suspend fun newStory(
        @Part("description") description: RequestBody,
        @Part file: MultipartBody.Part,
    ): NewStoryResponse
}