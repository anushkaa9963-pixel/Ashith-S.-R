package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.CustomRoutineEntity
import com.example.data.model.WorkoutSessionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface WorkoutLogDao {
    @Query("SELECT * FROM workout_session_logs ORDER BY timestamp DESC")
    fun getAllWorkoutSessions(): Flow<List<WorkoutSessionLog>>

    @Query("SELECT * FROM workout_session_logs WHERE dateKey = :dateKey")
    fun getSessionsForDate(dateKey: String): Flow<List<WorkoutSessionLog>>

    @Query("SELECT COUNT(*) FROM workout_session_logs")
    fun getTotalCompletedCount(): Flow<Int>

    @Query("SELECT COUNT(DISTINCT dateKey) FROM workout_session_logs")
    fun getTotalActiveDays(): Flow<Int>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSession(session: WorkoutSessionLog): Long
}

@Dao
interface CustomRoutineDao {
    @Query("SELECT * FROM custom_routines ORDER BY createdAt DESC")
    fun getAllCustomRoutines(): Flow<List<CustomRoutineEntity>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(routine: CustomRoutineEntity)

    @Query("DELETE FROM custom_routines WHERE id = :id")
    suspend fun deleteById(id: String)
}
