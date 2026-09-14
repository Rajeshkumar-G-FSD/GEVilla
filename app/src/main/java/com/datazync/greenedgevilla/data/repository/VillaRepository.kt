package com.datazync.greenedgevilla.data.repository

import com.datazync.greenedgevilla.data.local.BookingDao
import com.datazync.greenedgevilla.data.local.RoomDao
import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.Offer
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.data.model.UnitAvailabilityStatus
import kotlinx.coroutines.flow.Flow
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.random.Random

class VillaRepository(
    private val roomDao: RoomDao,
    private val bookingDao: BookingDao
) {
    val allUnits: Flow<List<RoomUnit>> = roomDao.getAllUnitsFlow()
    val allBookings: Flow<List<Booking>> = bookingDao.getAllBookingsFlow()

    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")

    fun getOffers(): List<Offer> {
        return listOf(
            Offer(
                id = "OFFER_WEEKEND",
                title = "Weekend Hill Getaway",
                subtitle = "Escape to the Nilgiris",
                code = "OOTYWKND15",
                discountPercent = 15,
                description = "Enjoy 15% discount on all Friday to Sunday stays. Includes complimentary tea plantation walking tour and evening hot cocoa.",
                validity = "Valid until Dec 31, 2026",
                applicableBhk = "All Rooms",
                badge = "POPULAR"
            ),
            Offer(
                id = "OFFER_FAMILY",
                title = "Family Villa Retreat",
                subtitle = "Luxury stays for big families",
                code = "FAMILYVILLA20",
                discountPercent = 20,
                description = "Flat 20% discount on complete 3 BHK Villas in Block A & B. Includes campfire setup, kids indoor board games, and free breakfast.",
                validity = "Valid for stays of 2+ nights",
                applicableBhk = "3 BHK",
                badge = "FAMILY SPECIAL"
            ),
            Offer(
                id = "OFFER_COUPLE",
                title = "Couple's Misty Serenity",
                subtitle = "Romantic 1 BHK Cottage",
                code = "MISTYLOVE",
                discountPercent = 10,
                description = "Romantic candlelight dinner setup in balcony and special aromatic Nilgiri floral bouquet on arrival at Block D 1 BHK.",
                validity = "Valid on all weekdays",
                applicableBhk = "1 BHK",
                badge = "COUPLE GETAWAY"
            ),
            Offer(
                id = "OFFER_LONGSTAY",
                title = "Long Stay Workation",
                subtitle = "Work amidst pine forests",
                code = "HILLWORK25",
                discountPercent = 25,
                description = "Stay 5 nights or more and save 25%! High-speed 300 Mbps fiber Wi-Fi, ergonomic desk setup, power backup, and laundry support.",
                validity = "Valid year-round for 5+ nights",
                applicableBhk = "All Rooms",
                badge = "WORKATION"
            ),
            Offer(
                id = "OFFER_SEASONAL",
                title = "Seasonal Ooty Blossom",
                subtitle = "Tea harvest seasonal treat",
                code = "BLOSSOM2026",
                discountPercent = 12,
                description = "Celebrate the Ooty tea season. Receive complimentary homemade Nilgiri chocolates and fresh green tea tasting kit.",
                validity = "Valid this season",
                applicableBhk = "All Rooms",
                badge = "LIMITED TIME"
            )
        )
    }

    /**
     * Checks whether a unit is available between checkIn and checkOut dates.
     * Prevents double booking for overlapping dates.
     */
    suspend fun isUnitAvailable(
        unit: RoomUnit,
        checkIn: LocalDate,
        checkOut: LocalDate,
        excludeBookingId: String? = null
    ): Boolean {
        if (unit.isBlocked) return false

        val bookings = bookingDao.getActiveBookingsForUnit(unit.id)
        for (b in bookings) {
            if (excludeBookingId != null && b.id == excludeBookingId) continue
            val bIn = runCatching { LocalDate.parse(b.checkInDate, dateFormatter) }.getOrNull() ?: continue
            val bOut = runCatching { LocalDate.parse(b.checkOutDate, dateFormatter) }.getOrNull() ?: continue

            // Overlap condition: checkIn < bOut && checkOut > bIn
            if (checkIn.isBefore(bOut) && checkOut.isAfter(bIn)) {
                return false
            }
        }
        return true
    }

    /**
     * Determine real-time operational status for a unit on a given date (e.g. today).
     */
    fun determineUnitStatus(
        unit: RoomUnit,
        targetDate: LocalDate,
        activeBookings: List<Booking>
    ): UnitAvailabilityStatus {
        if (unit.isBlocked) return UnitAvailabilityStatus.BLOCKED

        val unitBookings = activeBookings.filter { it.unitId == unit.id && it.bookingStatus != "Cancelled" }
        for (b in unitBookings) {
            val bIn = runCatching { LocalDate.parse(b.checkInDate, dateFormatter) }.getOrNull()
            val bOut = runCatching { LocalDate.parse(b.checkOutDate, dateFormatter) }.getOrNull()

            if (bIn != null && bOut != null) {
                if (bIn.isEqual(targetDate)) {
                    return UnitAvailabilityStatus.CHECK_IN_TODAY
                }
                if (bOut.isEqual(targetDate)) {
                    return UnitAvailabilityStatus.CHECK_OUT_TODAY
                }
                if (targetDate.isAfter(bIn) && targetDate.isBefore(bOut)) {
                    return UnitAvailabilityStatus.BOOKED
                }
            }
        }
        return UnitAvailabilityStatus.AVAILABLE
    }

    suspend fun createBooking(booking: Booking): Result<String> {
        val checkIn = runCatching { LocalDate.parse(booking.checkInDate, dateFormatter) }.getOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid check-in date"))
        val checkOut = runCatching { LocalDate.parse(booking.checkOutDate, dateFormatter) }.getOrNull()
            ?: return Result.failure(IllegalArgumentException("Invalid check-out date"))

        val unit = roomDao.getUnitById(booking.unitId)
            ?: return Result.failure(IllegalArgumentException("Room unit not found"))

        val available = isUnitAvailable(unit, checkIn, checkOut)
        if (!available) {
            return Result.failure(IllegalStateException("Unit ${booking.unitId} is already booked or blocked for selected dates."))
        }

        bookingDao.insertBooking(booking)
        return Result.success(booking.id)
    }

    suspend fun updateBookingStatus(bookingId: String, newStatus: String) {
        bookingDao.updateBookingStatus(bookingId, newStatus)
    }

    suspend fun updateRoomPrice(unitId: String, newPrice: Double) {
        roomDao.updatePrice(unitId, newPrice)
    }

    suspend fun toggleRoomBlock(unitId: String, isBlocked: Boolean, reason: String = "") {
        roomDao.updateBlockStatus(unitId, isBlocked, reason)
    }

    fun generateBookingId(): String {
        val randNum = Random.nextInt(1000, 9999)
        return "GEV-2026-$randNum"
    }
}
