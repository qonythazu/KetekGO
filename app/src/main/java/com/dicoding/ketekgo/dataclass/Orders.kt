package com.dicoding.ketekgo.dataclass

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Orders (
    val ketekName: String?,
    val ketekDestination: String?,
    val ketekTime: String?,
    val ketekTotalPrice: Int?,
    val ketekDate: String?,
    val ketekOrderer: String?
) : Parcelable