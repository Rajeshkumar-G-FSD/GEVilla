package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "room_units")
data class RoomUnit(
    @PrimaryKey val id: String, // e.g. "A1", "A2", "B1", "B2", "C1", "D1"
    val block: String,          // "Block A", "Block B", "Block C", "Block D"
    val bhkType: String,        // "3 BHK", "2 BHK", "1 BHK"
    val bedroomCount: Int,      // 3, 2, 1
    val cotCapacity: Int,       // 6, 3, 2
    val bedroomBreakdown: String, // e.g. "3 bedrooms (2 cots in each bedroom)"
    val maxGuests: Int,         // 8, 5, 3
    val pricePerNight: Double,  // e.g. 12000.0
    val isBlocked: Boolean = false,
    val blockedReason: String = "",
    val description: String,
    val amenities: List<String>,
    val houseRules: String,
    val checkInTime: String = "2:00 PM",
    val checkOutTime: String = "11:00 AM",
    val primaryImageResName: String = "resort_hero_ooty"
)

enum class UnitAvailabilityStatus {
    AVAILABLE,
    BOOKED,
    BLOCKED,
    CHECK_IN_TODAY,
    CHECK_OUT_TODAY
}
