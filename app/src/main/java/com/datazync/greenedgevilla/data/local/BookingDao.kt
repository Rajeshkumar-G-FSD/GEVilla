package com.datazync.greenedgevilla.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.datazync.greenedgevilla.data.model.Booking
import kotlinx.coroutines.flow.Flow

@Dao
interface BookingDao {
    @Query("SELECT * FROM bookings ORDER BY createdTimestamp DESC")
    fun getAllBookingsFlow(): Flow<List<Booking>>

    @Query("SELECT * FROM bookings ORDER BY createdTimestamp DESC")
    suspend fun getAllBookings(): List<Booking>

    @Query("SELECT * FROM bookings WHERE id = :id LIMIT 1")
    suspend fun getBookingById(id: String): Booking?

    @Query("SELECT * FROM bookings WHERE unitId = :unitId AND bookingStatus != 'Cancelled'")
    suspend fun getActiveBookingsForUnit(unitId: String): List<Booking>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBooking(booking: Booking)

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertBookings(bookings: List<Booking>)

    @Update
    suspend fun updateBooking(booking: Booking)

    @Query("UPDATE bookings SET bookingStatus = :status WHERE id = :id")
    suspend fun updateBookingStatus(id: String, status: String)

    @Query("DELETE FROM bookings WHERE id = :id")
    suspend fun deleteBookingById(id: String)

    @Query("SELECT COUNT(*) FROM bookings")
    suspend fun getBookingCount(): Int
}
