package com.datazync.greenedgevilla.data.repository

import com.datazync.greenedgevilla.data.model.Booking
import com.datazync.greenedgevilla.data.model.Offer
import com.datazync.greenedgevilla.data.model.RoomUnit
import com.datazync.greenedgevilla.data.model.UnitAvailabilityStatus
import com.datazync.greenedgevilla.data.remote.BOOKINGS_COLLECTION
import com.datazync.greenedgevilla.data.remote.SeedData
import com.datazync.greenedgevilla.data.remote.UNITS_COLLECTION
import com.datazync.greenedgevilla.data.remote.toBooking
import com.datazync.greenedgevilla.data.remote.toFirestoreMap
import com.datazync.greenedgevilla.data.remote.toRoomUnit
import com.google.android.gms.tasks.Task
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.FirebaseFirestoreException
import com.google.firebase.firestore.Source
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.suspendCancellableCoroutine
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import kotlin.coroutines.resume
import kotlin.coroutines.resumeWithException
import kotlin.random.Random

/**
 * Firestore-backed source of truth for room units & bookings.
 *
 * Both [allUnits] and [allBookings] are live-updating [StateFlow]s fed by a single
 * long-lived `addSnapshotListener` each (registered once, for the lifetime of this
 * repository instance) — so every screen sees the same, always-current data with no
 * per-screen re-subscription cost, and writes from any screen (or another device)
 * reflect everywhere instantly without any manual reload.
 */
class VillaRepository(
    private val firestore: FirebaseFirestore
) {
    private val dateFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd")
    private val repoScope = CoroutineScope(SupervisorJob() + Dispatchers.IO)

    private val _allUnits = MutableStateFlow<List<RoomUnit>>(emptyList())
    val allUnits: StateFlow<List<RoomUnit>> = _allUnits.asStateFlow()

    private val _allBookings = MutableStateFlow<List<Booking>>(emptyList())
    val allBookings: StateFlow<List<Booking>> = _allBookings.asStateFlow()

    /** Surfaces the most recent Firestore listener error, if any, so the UI can show it. */
    private val _syncError = MutableStateFlow<String?>(null)
    val syncError: StateFlow<String?> = _syncError.asStateFlow()

    init {
        firestore.collection(UNITS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _syncError.value = error.readableMessage()
                    return@addSnapshotListener
                }
                _syncError.value = null
                _allUnits.value = snapshot?.documents
                    ?.mapNotNull { it.toRoomUnit() }
                    ?.sortedBy { it.id }
                    ?: emptyList()
            }

        firestore.collection(BOOKINGS_COLLECTION)
            .addSnapshotListener { snapshot, error ->
                if (error != null) {
                    _syncError.value = error.readableMessage()
                    return@addSnapshotListener
                }
                _syncError.value = null
                _allBookings.value = snapshot?.documents
                    ?.mapNotNull { it.toBooking() }
                    ?.sortedByDescending { it.createdTimestamp }
                    ?: emptyList()
            }

        repoScope.launch { seedIfEmpty() }
    }

    /** Writes the built-in demo catalog to Firestore once, only if the collections are empty. */
    private suspend fun seedIfEmpty() {
        runCatching {
            val unitsSnapshot = firestore.collection(UNITS_COLLECTION).get(Source.SERVER).awaitTask()
            if (unitsSnapshot.isEmpty) {
                val batch = firestore.batch()
                SeedData.units.forEach { unit ->
                    batch.set(firestore.collection(UNITS_COLLECTION).document(unit.id), unit.toFirestoreMap())
                }
                batch.commit().awaitTask()
            }

            val bookingsSnapshot = firestore.collection(BOOKINGS_COLLECTION).get(Source.SERVER).awaitTask()
            if (bookingsSnapshot.isEmpty) {
                val batch = firestore.batch()
                SeedData.bookings.forEach { booking ->
                    batch.set(firestore.collection(BOOKINGS_COLLECTION).document(booking.id), booking.toFirestoreMap())
                }
                batch.commit().awaitTask()
            }
        }.onFailure { e ->
            _syncError.value = (e as? FirebaseFirestoreException)?.readableMessage() ?: e.message
        }
    }

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
     * Prevents double booking for overlapping dates. Reads from the live in-memory
     * booking cache (always current — see the snapshot listener above) so this never
     * blocks on a network round trip.
     */
    fun isUnitAvailable(
        unit: RoomUnit,
        checkIn: LocalDate,
        checkOut: LocalDate,
        excludeBookingId: String? = null
    ): Boolean {
        if (unit.isBlocked) return false

        val bookings = _allBookings.value.filter { it.unitId == unit.id && it.bookingStatus != "Cancelled" }
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

        val unit = _allUnits.value.find { it.id == booking.unitId }
            ?: return Result.failure(IllegalArgumentException("Room unit not found"))

        val available = isUnitAvailable(unit, checkIn, checkOut)
        if (!available) {
            return Result.failure(IllegalStateException("Unit ${booking.unitId} is already booked or blocked for selected dates."))
        }

        return runCatching {
            firestore.collection(BOOKINGS_COLLECTION)
                .document(booking.id)
                .set(booking.toFirestoreMap())
                .awaitTask()
            booking.id
        }.recoverCatching { e ->
            throw IllegalStateException(readableFailure(e), e)
        }
    }

    suspend fun updateBookingStatus(bookingId: String, newStatus: String) {
        runCatching {
            firestore.collection(BOOKINGS_COLLECTION)
                .document(bookingId)
                .update("bookingStatus", newStatus)
                .awaitTask()
        }.onFailure { _syncError.value = readableFailure(it) }
    }

    suspend fun updateRoomPrice(unitId: String, newPrice: Double) {
        runCatching {
            firestore.collection(UNITS_COLLECTION)
                .document(unitId)
                .update("pricePerNight", newPrice)
                .awaitTask()
        }.onFailure { _syncError.value = readableFailure(it) }
    }

    suspend fun toggleRoomBlock(unitId: String, isBlocked: Boolean, reason: String = "") {
        runCatching {
            firestore.collection(UNITS_COLLECTION)
                .document(unitId)
                .update(mapOf("isBlocked" to isBlocked, "blockedReason" to reason))
                .awaitTask()
        }.onFailure { _syncError.value = readableFailure(it) }
    }

    /** Generates a booking id guaranteed not to collide with any currently known booking. */
    fun generateBookingId(): String {
        var id: String
        do {
            id = "GEV-2026-${Random.nextInt(1000, 9999)}"
        } while (_allBookings.value.any { it.id == id })
        return id
    }

    /**
     * Forces a fresh server round trip for both collections. The live snapshot listeners
     * above pick up any resulting changes automatically, so callers just await this and
     * then know the on-screen data reflects the server.
     */
    suspend fun refreshNow(): Result<Unit> = runCatching {
        firestore.collection(UNITS_COLLECTION).get(Source.SERVER).awaitTask()
        firestore.collection(BOOKINGS_COLLECTION).get(Source.SERVER).awaitTask()
        _syncError.value = null
    }.onFailure { _syncError.value = readableFailure(it) }
        .map { }
}

private fun readableFailure(t: Throwable): String =
    (t as? FirebaseFirestoreException)?.readableMessage() ?: (t.message ?: "Something went wrong. Please try again.")

private fun FirebaseFirestoreException.readableMessage(): String = when (code) {
    FirebaseFirestoreException.Code.PERMISSION_DENIED -> {
        val raw = message.orEmpty()
        if (raw.contains("API", ignoreCase = true) && raw.contains("has not been used", ignoreCase = true)) {
            "Firestore isn't enabled for this Firebase project yet. Open the Firebase console, create a Firestore database, then try again."
        } else {
            "Firestore denied this request. Check the security rules for this project."
        }
    }
    FirebaseFirestoreException.Code.UNAVAILABLE ->
        "Can't reach Firebase right now. Check your internet connection."
    FirebaseFirestoreException.Code.NOT_FOUND ->
        "That record no longer exists."
    else -> message ?: "Firebase error ($code)."
}

/** Minimal, dependency-free `Task<T>.await()` so we don't need the play-services-coroutines artifact. */
private suspend fun <T> Task<T>.awaitTask(): T = suspendCancellableCoroutine { cont ->
    addOnSuccessListener { result -> cont.resume(result) }
    addOnFailureListener { exception -> cont.resumeWithException(exception) }
    addOnCanceledListener {
        if (cont.isActive) cont.resumeWithException(kotlinx.coroutines.CancellationException("Task was cancelled"))
    }
}
