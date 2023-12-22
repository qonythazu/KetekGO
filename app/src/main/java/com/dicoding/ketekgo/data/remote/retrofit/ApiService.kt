package com.dicoding.ketekgo.data.remote.retrofit

import com.dicoding.ketekgo.dataclass.DestinationResponse
import com.dicoding.ketekgo.dataclass.PreferencesRequest
import retrofit2.http.Body
import retrofit2.http.Field
import retrofit2.http.FormUrlEncoded
import retrofit2.http.POST

interface ApiService {

    @POST("recomendations_destination")
    suspend fun recomendDestination(
        @Body preferencesRequest: PreferencesRequest,
    ): DestinationResponse

}