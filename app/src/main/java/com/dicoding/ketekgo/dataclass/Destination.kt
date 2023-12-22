package com.dicoding.ketekgo.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Destination (
    val photo: Int? = null,
    val name: String
) : Parcelable