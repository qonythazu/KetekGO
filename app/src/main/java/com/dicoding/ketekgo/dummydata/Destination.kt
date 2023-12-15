package com.dicoding.ketekgo.dummydata

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination (
    val photo: Int,
    val name: String
) : Parcelable