package com.dicoding.submissionaplikasistoryapp.model

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.dicoding.submissionaplikasistoryapp.data.RegisterResponse
import com.dicoding.submissionaplikasistoryapp.repository.RegisterRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import java.lang.Exception
import javax.inject.Inject

@HiltViewModel
class RegisterViewModel @Inject constructor(private val registerRepository: RegisterRepository):
    ViewModel() {
    sealed class ResultRegister<out T: Any>{
        data class Success<out T : Any>(val data : T) : ResultRegister<T>()
        data class Error(val error : String) : ResultRegister<Nothing>()
    }

    val isLoading : LiveData<Boolean>
        get() = registerRepository.isLoading

    suspend fun userRegister(name:String, email : String, password: String): ResultRegister<RegisterResponse>{
        return try {
            when(val response = registerRepository.register(name, email, password)){
                is RegisterRepository.ResultRegister.Success -> ResultRegister.Success(response.data)
                is RegisterRepository.ResultRegister.Error -> ResultRegister.Error(response.message)
            }
        }catch (e: Exception){
            ResultRegister.Error(e.toString())
        }
    }
}