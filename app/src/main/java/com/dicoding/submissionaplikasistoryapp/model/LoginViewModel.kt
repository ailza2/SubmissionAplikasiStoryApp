package com.dicoding.submissionaplikasistoryapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionaplikasistoryapp.data.LoginResponse
import com.dicoding.submissionaplikasistoryapp.repository.LoginRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class LoginViewModel @Inject constructor(private val loginRepository: LoginRepository): ViewModel(){
    sealed class ResultLogin<out T : Any> {
        data class Success<out T : Any>(val data: T) : ResultLogin<T>()
        data class Error(val error: String) : ResultLogin<Nothing>()
    }

    val showLoading : LiveData<Boolean>
        get() = loginRepository.showLoading
    suspend fun loginUser(email:String, password:String):ResultLogin<LoginResponse>{
        return try{
            when(val result = loginRepository.login(email, password)){
                is LoginRepository.ResultLogin.Success -> ResultLogin.Success(result.data)
                is LoginRepository.ResultLogin.Error -> ResultLogin.Error(result.message)
            }
        }catch (e: Exception){
            ResultLogin.Error(e.toString())
        }
    }
}