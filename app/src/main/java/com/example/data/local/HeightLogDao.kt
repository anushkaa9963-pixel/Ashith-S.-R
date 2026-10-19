package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.HeightLog
import kotlinx.coroutines.flow.Flow

@Dao
interface HeightLogDao {
    @Query("SELECT * FROM height_logs ORDER BY timestamp ASC")
    fun getAllHeightLogsAsc(): Flow<List<HeightLog>>

    @Query("SELECT * FROM height_logs ORDER BY timestamp DESC")
    fun getAllHeightLogsDesc(): Flow<List<HeightLog>>

    @Query("SELECT * FROM height_logs ORDER BY timestamp DESC LIMIT 1")
    fun getLatestHeightLog(): Flow<HeightLog?>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insert(heightLog: HeightLog): Long

    @Query("DELETE FROM height_logs WHERE id = :id")
    suspend fun deleteById(id: Long)
}
