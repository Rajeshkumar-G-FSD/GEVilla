package com.datazync.greenedgevilla.data.remote

import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.google.firebase.firestore.DocumentSnapshot

/**
 * Manual (reflection-free) Firestore <-> model mapping.
 *
 * We map explicitly instead of relying on Firestore's automatic POJO mapping so that:
 *  - numeric fields survive Firestore's Long/Double round-trip without crashing,
 *  - every field always has a safe fallback if a document is partially written, and
 *  - no extra `kotlin-reflect` dependency is required at runtime.
 */

const val UNITS_COLLECTION = "room_units"
const val BOOKINGS_COLLECTION = "bookings"

fun RoomUnit.toFirestoreMap(): Map<String, Any?> = mapOf(
    "id" to id,
    "block" to block,
    "bhkType" to bhkType,
    "bedroomCount" to bedroomCount,
    "cotCapacity" to cotCapacity,
    "bedroomBreakdown" to bedroomBreakdown,
    "maxGuests" to maxGuests,
    "pricePerNight" to pricePerNight,
    "isBlocked" to isBlocked,
    "blockedReason" to blockedReason,
    "description" to description,
    "amenities" to amenities,
    "houseRules" to houseRules,
    "checkInTime" to checkInTime,
    "checkOutTime" to checkOutTime,
    "primaryImageResName" to primaryImageResName
)

fun DocumentSnapshot.toRoomUnit(): RoomUnit? {
    val data = data ?: return null
    return RoomUnit(
        id = id,
        block = data["block"] as? String ?: "",
        bhkType = data["bhkType"] as? String ?: "",
        bedroomCount = (data["bedroomCount"] as? Number)?.toInt() ?: 0,
        cotCapacity = (data["cotCapacity"] as? Number)?.toInt() ?: 0,
        bedroomBreakdown = data["bedroomBreakdown"] as? String ?: "",
        maxGuests = (data["maxGuests"] as? Number)?.toInt() ?: 0,
        pricePerNight = (data["pricePerNight"] as? Number)?.toDouble() ?: 0.0,
        isBlocked = data["isBlocked"] as? Boolean ?: false,
        blockedReason = data["blockedReason"] as? String ?: "",
        description = data["description"] as? String ?: "",
        amenities = (data["amenities"] as? List<*>)?.mapNotNull { it as? String } ?: emptyList(),
        houseRules = data["houseRules"] as? String ?: "",
        checkInTime = data["checkInTime"] as? String ?: "2:00 PM",
        checkOutTime = data["checkOutTime"] as? String ?: "11:00 AM",
        primaryImageResName = data["primaryImageResName"] as? String ?: "resort_hero_ooty"
    )
}

fun Booking.toFirestoreMap(): Map<String, Any?> = mapOf(
    "id" to id,
    "unitId" to unitId,
    "block" to block,
    "bhkType" to bhkType,
    "guestName" to guestName,
    "guestMobile" to guestMobile,
    "guestEmail" to guestEmail,
    "adults" to adults,
    "children" to children,
    "checkInDate" to checkInDate,
    "checkOutDate" to checkOutDate,
    "numberOfNights" to numberOfNights,
    "roomPrice" to roomPrice,
    "taxes" to taxes,
    "totalAmount" to totalAmount,
    "specialRequests" to specialRequests,
    "paymentMethod" to paymentMethod,
    "paymentStatus" to paymentStatus,
    "bookingStatus" to bookingStatus,
    "source" to source,
    "referenceName" to referenceName,
    "createdTimestamp" to createdTimestamp
)

fun DocumentSnapshot.toBooking(): Booking? {
    val data = data ?: return null
    return Booking(
        id = id,
        unitId = data["unitId"] as? String ?: "",
        block = data["block"] as? String ?: "",
        bhkType = data["bhkType"] as? String ?: "",
        guestName = data["guestName"] as? String ?: "",
        guestMobile = data["guestMobile"] as? String ?: "",
        guestEmail = data["guestEmail"] as? String ?: "",
        adults = (data["adults"] as? Number)?.toInt() ?: 0,
        children = (data["children"] as? Number)?.toInt() ?: 0,
        checkInDate = data["checkInDate"] as? String ?: "",
        checkOutDate = data["checkOutDate"] as? String ?: "",
        numberOfNights = (data["numberOfNights"] as? Number)?.toInt() ?: 0,
        roomPrice = (data["roomPrice"] as? Number)?.toDouble() ?: 0.0,
        taxes = (data["taxes"] as? Number)?.toDouble() ?: 0.0,
        totalAmount = (data["totalAmount"] as? Number)?.toDouble() ?: 0.0,
        specialRequests = data["specialRequests"] as? String ?: "",
        paymentMethod = data["paymentMethod"] as? String ?: "UPI",
        paymentStatus = data["paymentStatus"] as? String ?: "Paid",
        bookingStatus = data["bookingStatus"] as? String ?: "Upcoming",
        source = data["source"] as? String ?: "Direct App",
        referenceName = data["referenceName"] as? String ?: "",
        createdTimestamp = (data["createdTimestamp"] as? Number)?.toLong() ?: 0L
    )
}
