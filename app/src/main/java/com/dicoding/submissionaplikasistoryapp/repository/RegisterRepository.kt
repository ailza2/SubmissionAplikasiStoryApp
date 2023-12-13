package com.dicoding.submissionaplikasistoryapp.repository

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.dicoding.submissionaplikasistoryapp.data.RegisterResponse
import com.dicoding.submissionaplikasistoryapp.service.RegisterService
import java.lang.Exception
import javax.inject.Inject

class RegisterRepository @Inject constructor(private val registerService: RegisterService){
    sealed class ResultRegister<out T: Any> {
        data class Success<out T : Any>(val data: T) : ResultRegister<T>()
        data class Error(val message: String) : ResultRegister<Nothing>()
    }

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean>
        get() = _isLoading

    suspend fun register(
        name: String,
        email: String,
        password: String
    ): ResultRegister<RegisterResponse> {
        _isLoading.value = true
        return try {
            val response = registerService.userRegister(name, email, password)
            _isLoading.value = false
            ResultRegister.Success(response)
        }catch (e: Exception){
            _isLoading.value = false
            ResultRegister.Error(e.toString())
        }
    }
}