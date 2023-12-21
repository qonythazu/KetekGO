package com.dicoding.ketekgo.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val ketekName: String,
    val ketekDestination: String,
    val ketekTime: String,
    val ketekPrice: String,
    val ketekPassengers: Int,
    val status: String,
    val date: String
) : Parcelable
