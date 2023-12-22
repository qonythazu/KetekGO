package com.dicoding.ketekgo.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.dicoding.ketekgo.data.remote.retrofit.ApiService
import com.dicoding.ketekgo.dataclass.DestinationResponse
import com.dicoding.ketekgo.dataclass.PreferencesRequest

class AppRepository(
    private val apiService: ApiService
) {

    fun recomendDestination(preferences: PreferencesRequest): LiveData<Result<DestinationResponse>> =
        liveData {
            emit(Result.Loading)
            try {
                val destinationResponse = apiService.recomendDestination(preferences)
                if (destinationResponse.topLabels != null) {
                    emit(Result.Success(destinationResponse))
                }
            } catch (e: Exception) {
                emit(Result.Error(e.message.toString()))
            }
        }

    companion object {
        @Volatile
        private var instance: AppRepository? = null
        fun getInstance(
            apiService: ApiService,
        ): AppRepository =
            instance ?: synchronized(this) {
                instance ?: AppRepository(apiService)
            }.also { instance = it }
    }
}