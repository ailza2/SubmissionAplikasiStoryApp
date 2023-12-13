package com.dicoding.submissionaplikasistoryapp.service

import android.content.SharedPreferences
import androidx.core.content.edit
import com.dicoding.submissionaplikasistoryapp.model.UserModel

class SettingPreferences(private val sharedPreferences: SharedPreferences) {

    fun saveUser(userModel: UserModel) {
        sharedPreferences.edit {
            putString(USER_ID, userModel.userId)
            putString(USER_NAME, userModel.name)
            putString(USER_TOKEN, userModel.token)
        }
    }

    fun getUser(): UserModel? {
        val userId = sharedPreferences.getString(USER_ID, null)
        val name = sharedPreferences.getString(USER_NAME, null)
        val token = sharedPreferences.getString(USER_TOKEN, null)
        return if (userId != null && name != null && token != null) {
            UserModel(userId, name, token)

        } else {
            null
        }
    }

    fun clearUser() {
        sharedPreferences.edit {
            this.clear()
        }
    }

    companion object {
        const val USER_ID = "user_id"
        const val USER_NAME = "user_name"
        const val USER_TOKEN = "user token"
    }
}