package com.dicoding.ketekgo.dataclass

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.parcelize.Parcelize

@Parcelize
data class PreferencesRequest(
    @SerializedName("preferences")
    val preferences: List<String>
) : Parcelable
