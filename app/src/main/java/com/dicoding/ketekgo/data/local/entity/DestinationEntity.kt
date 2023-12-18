package com.dicoding.ketekgo.data.local.entity

import android.net.eap.EapSessionConfig.EapAkaPrimeConfig
import android.os.Parcelable
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "destination_table")
class DestinationEntity (
    @ColumnInfo(name = "id")
    @PrimaryKey
    var destinationId: String,

    @ColumnInfo(name = "place_name")
    var placeName: String,

    @ColumnInfo(name = "location")
    var location: String,

    @ColumnInfo(name = "description")
    var description: String,

    @ColumnInfo(name = "price")
    var price: String
) : Parcelable