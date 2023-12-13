package com.dicoding.submissionaplikasistoryapp.service


import com.dicoding.submissionaplikasistoryapp.data.RegisterResponse
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface RegisterService {
    @FormUrlEncoded
    @POST("register")
    suspend fun userRegister(
        @Field("name")name:String,
        @Field("email")email: String,
        @Field("password")password:String
    ) : RegisterResponse
}