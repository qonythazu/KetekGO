package com.dicoding.ketekgo.data.repository

import android.content.Context
import com.dicoding.ketekgo.data.remote.retrofit.ApiConfig

private const val USER_PREFERENCES = "user_preferences"

object Injection {
    fun provideRepository(context: Context): AppRepository {
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(
            apiService
        )
    }
}