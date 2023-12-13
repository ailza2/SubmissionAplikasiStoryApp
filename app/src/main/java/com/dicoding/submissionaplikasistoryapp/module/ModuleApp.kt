package com.dicoding.submissionaplikasistoryapp.module

import android.app.Application
import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.room.Room
import com.dicoding.submissionaplikasistoryapp.database.StoryDatabase
import com.dicoding.submissionaplikasistoryapp.location.MapsService
import com.dicoding.submissionaplikasistoryapp.service.DetailService
import com.dicoding.submissionaplikasistoryapp.service.LoginService
import com.dicoding.submissionaplikasistoryapp.service.MainService
import com.dicoding.submissionaplikasistoryapp.service.NewStoryService
import com.dicoding.submissionaplikasistoryapp.service.RegisterService
import com.dicoding.submissionaplikasistoryapp.service.SettingPreferences.Companion.USER_TOKEN
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object NetworkModule {
    private const val base_url = "https://story-api.dicoding.dev/v1/"

    @Provides
    @Singleton
    fun provideOkHttpClient(sharedPreferences: SharedPreferences): OkHttpClient {
        return OkHttpClient.Builder()
            .addInterceptor { chain ->
                val requestBuilder = chain.request().newBuilder()
                val token = sharedPreferences.getString(USER_TOKEN, "")
                Log.d("cekTokenInRetrofit", "$token")
                if (!token.isNullOrEmpty()) {
                    requestBuilder.addHeader("Authorization", "Bearer $token")
                }
                chain.proceed(requestBuilder.build())
            }
            .build()
    }

    @Provides
    @Singleton
    fun provideRetrofit(okHttpClient: OkHttpClient): Retrofit {
        return Retrofit.Builder()
            .baseUrl(base_url)
            .client(okHttpClient)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    @Provides
    @Singleton
    fun provideRegisterService(retrofit: Retrofit): RegisterService {
        return retrofit.create(RegisterService::class.java)
    }

    @Provides
    @Singleton
    fun provideLoginService(retrofit: Retrofit): LoginService {
        return retrofit.create(LoginService::class.java)
    }

    @Provides
    @Singleton
    fun provideMainService(retrofit: Retrofit): MainService {
        return retrofit.create(MainService::class.java)
    }

    @Provides
    @Singleton
    fun provideNewStoryService(retrofit: Retrofit): NewStoryService {
        return retrofit.create(NewStoryService::class.java)
    }

    @Provides
    @Singleton
    fun provideDetailStoryService(retrofit: Retrofit): DetailService {
        return retrofit.create(DetailService::class.java)
    }

    @Provides
    @Singleton
    fun provideMapsService(retrofit: Retrofit): MapsService {
        return retrofit.create(MapsService::class.java)
    }
}

@Module
@InstallIn(SingletonComponent::class)
object LocalModule {
    @Singleton
    @Provides
    fun provideSharedPreferences(application: Application): SharedPreferences {
        return application.getSharedPreferences("userPreferences", Context.MODE_PRIVATE)
    }

    @Singleton
    @Provides
    fun provideStoryDatabase(application: Application): StoryDatabase {
        return Room.databaseBuilder(
            application.applicationContext,
            StoryDatabase::class.java,
            "story_database"
        ).build()
    }
}