package com.datazync.greenedgevilla.data.model

data class Offer(
    val id: String,
    val title: String,
    val subtitle: String,
    val code: String,
    val discountPercent: Int,
    val description: String,
    val validity: String,
    val applicableBhk: String = "All Rooms",
    val badge: String = "EXCLUSIVE"
)
