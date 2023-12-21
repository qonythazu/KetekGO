package com.dicoding.ketekgo.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class KetekDriver (
    val photo: String?,
    val name: String?,
    val from: String?,
    val to: String?,
    val time: String?,
    val capacity: Int?,
    val price: String?
) : Parcelable