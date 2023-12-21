package com.dicoding.ketekgo.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination (
    val photo: Int,
    val name: String
) : Parcelable