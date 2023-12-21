package com.dicoding.ketekgo.dataclass

data class Booking(
    val name: String,
    val destination: String,
    val time: String,
    val passengers: Int,
    val totalPrice: Int
)
