package com.dicoding.ketekgo.data

import com.dicoding.ketekgo.data.remote.retrofit.ApiConfig

object Injection {
    fun provideRepository(): AppRepository {
        val apiService = ApiConfig.getApiService()
        return AppRepository.getInstance(
            apiService,
        )
    }
}