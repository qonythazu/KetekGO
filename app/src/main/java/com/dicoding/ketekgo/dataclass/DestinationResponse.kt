package com.dicoding.ketekgo.dataclass

import com.google.gson.annotations.SerializedName

data class DestinationResponse(

    @field:SerializedName("top_labels")
    val topLabels: List<String?>? = null
)
