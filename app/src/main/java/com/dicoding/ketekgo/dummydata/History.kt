package com.dicoding.ketekgo.dummydata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class History(
    val ketekName: String,
    val ketekFrom: String,
    val ketekTo: String,
    val ketekTime: String,
    val ketekPrice: String,
    val status: String,
    val date: String
) : Parcelable
