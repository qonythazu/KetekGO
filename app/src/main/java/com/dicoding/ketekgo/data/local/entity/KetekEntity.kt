package com.dicoding.ketekgo.data.local.entity

import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "ketek_table")
class KetekEntity (
    @ColumnInfo(name = "id")
    @PrimaryKey
    var ketekId: String,

    @ColumnInfo(name = "place_start")
    var placeStart: String,

    @ColumnInfo(name = "place_end")
    var placeEnd: String,

    @ColumnInfo(name = "capacity")
    var capacity: String,

    @ColumnInfo(name = "time")
    var time: String
) : Parcelable