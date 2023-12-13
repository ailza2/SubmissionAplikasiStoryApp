package com.dicoding.submissionaplikasistoryapp.module

import android.content.SharedPreferences
import com.dicoding.submissionaplikasistoryapp.database.StoryDatabase
import com.dicoding.submissionaplikasistoryapp.location.MapsRepository
import com.dicoding.submissionaplikasistoryapp.location.MapsService
import com.dicoding.submissionaplikasistoryapp.location.MapsViewModel
import com.dicoding.submissionaplikasistoryapp.model.MainViewModel
import com.dicoding.submissionaplikasistoryapp.model.NewStoryViewModel
import com.dicoding.submissionaplikasistoryapp.model.DetailViewModel
import com.dicoding.submissionaplikasistoryapp.model.LoginViewModel
import com.dicoding.submissionaplikasistoryapp.model.RegisterViewModel
import com.dicoding.submissionaplikasistoryapp.repository.DetailRepository
import com.dicoding.submissionaplikasistoryapp.repository.LoginRepository
import com.dicoding.submissionaplikasistoryapp.repository.MainRepository
import com.dicoding.submissionaplikasistoryapp.repository.NewStoryRepository
import com.dicoding.submissionaplikasistoryapp.repository.RegisterRepository
import com.dicoding.submissionaplikasistoryapp.service.DetailService
import com.dicoding.submissionaplikasistoryapp.service.LoginService
import com.dicoding.submissionaplikasistoryapp.service.MainService
import com.dicoding.submissionaplikasistoryapp.service.NewStoryService
import com.dicoding.submissionaplikasistoryapp.service.RegisterService
import com.dicoding.submissionaplikasistoryapp.service.SettingPreferences
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object RepositoryModule {
    @Provides
    @Singleton
    fun provideRegisterRepository(registerService: RegisterService) =
        RegisterRepository(registerService)

    @Provides
    @Singleton
    fun provideLoginRepository(loginService: LoginService) = LoginRepository(loginService)

    @Provides
    @Singleton
    fun provideMainRepository(storyDatabase: StoryDatabase, mainService: MainService) =
        MainRepository(storyDatabase, mainService)

    @Provides
    @Singleton
    fun provideDetailRepository(detailStoryService: DetailService) =
        DetailRepository(detailStoryService)

    @Provides
    @Singleton
    fun provideNewStoryRepository(newStoryService: NewStoryService) =
        NewStoryRepository(newStoryService)

    @Provides
    @Singleton
    fun provideStoryCoordinatesRepository(mapsService: MapsService) = MapsRepository(mapsService)
}

@Module
@InstallIn(SingletonComponent::class)
object ViewModelModule {
    @Provides
    @Singleton
    fun provideRegisterViewModel(registerRepository: RegisterRepository) =
        RegisterViewModel(registerRepository)

    @Provides
    @Singleton
    fun provideLoginViewModel(loginRepository: LoginRepository) = LoginViewModel(loginRepository)

    @Provides
    @Singleton
    fun provideMainViewModel(mainRepository: MainRepository) = MainViewModel(mainRepository)

    @Provides
    @Singleton
    fun provideNewStoryViewModel(newStoryRepository: NewStoryRepository) =
        NewStoryViewModel(newStoryRepository)

    @Provides
    @Singleton
    fun provideDetailViewModel(detailRepository: DetailRepository) =
        DetailViewModel(detailRepository)

    @Provides
    @Singleton
    fun provideMapsViewModel(mapsRepository: MapsRepository) = MapsViewModel(mapsRepository)
}

@Module
@InstallIn(SingletonComponent::class)
object SharedPreferencesModule {
    @Provides
    @Singleton
    fun provideUserPreferences(sharedPreferences: SharedPreferences) =
        SettingPreferences(sharedPreferences)
}