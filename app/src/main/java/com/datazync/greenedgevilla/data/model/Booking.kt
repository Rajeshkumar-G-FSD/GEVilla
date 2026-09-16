package com.datazync.greenedgevilla.data.model

/** Stored in Firestore under the "bookings" collection, one document per booking (doc id == [id]). */
data class Booking(
    val id: String,                      // e.g. "GEV-2026-8492"
    val unitId: String,                  // "A1", "A2", "B1", "B2", "C1", "D1"
    val block: String,                   // "Block A", etc.
    val bhkType: String,                 // "3 BHK", etc.
    val guestName: String,
    val guestMobile: String,
    val guestEmail: String,
    val adults: Int,
    val children: Int,
    val checkInDate: String,             // "YYYY-MM-DD" e.g. "2026-09-14"
    val checkOutDate: String,            // "YYYY-MM-DD" e.g. "2026-09-16"
    val numberOfNights: Int,
    val roomPrice: Double,
    val taxes: Double,
    val totalAmount: Double,
    val specialRequests: String = "",
    val paymentMethod: String = "UPI",   // "UPI", "Credit/Debit Card", "Net Banking", "Pay at Hotel"
    val paymentStatus: String = "Paid",  // "Paid", "Pending", "Refunded"
    val bookingStatus: String = "Upcoming", // "Upcoming", "Current Stay", "Completed", "Cancelled"
    val source: String = "Direct App",   // "Direct App", "Agoda", "MakeMyTrip", "VOYE", "Booking.com", "Goibibo", "Walk-in"
    val referenceName: String = "",      // Reference/Referral name e.g. "Owner Reference", "Suresh Cabs"
    val createdTimestamp: Long = System.currentTimeMillis()
)
