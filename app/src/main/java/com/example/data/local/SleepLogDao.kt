package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.SleepLog
import kotlinx.coroutines.flow.Flow

@Dao
interface SleepLogDao {
    @Query("SELECT * FROM sleep_logs WHERE dateKey = :dateKey LIMIT 1")
    fun getSleepForDate(dateKey: String): Flow<SleepLog?>

    @Query("SELECT * FROM sleep_logs ORDER BY dateKey DESC LIMIT 30")
    fun getRecentSleepLogs(): Flow<List<SleepLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(sleepLog: SleepLog)

    @Query("SELECT COUNT(*) FROM sleep_logs WHERE sleptDuringHghWindow = 1")
    fun getHghOptimalWindowCount(): Flow<Int>
}
