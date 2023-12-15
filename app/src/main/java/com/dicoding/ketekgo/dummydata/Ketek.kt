package com.dicoding.ketekgo.dummydata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Ketek(
    val username: String,
    val photo: Int,
    val name: String,
    val from: String,
    val to: String,
    val time: String,
    val capacity: Int,
    val price: String
) : Parcelable