package com.datazync.greenedgevilla.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.datazync.greenedgevilla.data.model.RoomUnit
import kotlinx.coroutines.flow.Flow

@Dao
interface RoomDao {
    @Query("SELECT * FROM room_units ORDER BY id ASC")
    fun getAllUnitsFlow(): Flow<List<RoomUnit>>

    @Query("SELECT * FROM room_units ORDER BY id ASC")
    suspend fun getAllUnits(): List<RoomUnit>

    @Query("SELECT * FROM room_units WHERE id = :id LIMIT 1")
    suspend fun getUnitById(id: String): RoomUnit?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertUnits(units: List<RoomUnit>)

    @Update
    suspend fun updateUnit(unit: RoomUnit)

    @Query("UPDATE room_units SET pricePerNight = :price WHERE id = :id")
    suspend fun updatePrice(id: String, price: Double)

    @Query("UPDATE room_units SET isBlocked = :isBlocked, blockedReason = :reason WHERE id = :id")
    suspend fun updateBlockStatus(id: String, isBlocked: Boolean, reason: String)

    @Query("SELECT COUNT(*) FROM room_units")
    suspend fun getUnitCount(): Int
}
