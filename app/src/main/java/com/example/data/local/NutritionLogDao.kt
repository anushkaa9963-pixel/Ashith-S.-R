package com.example.data.local

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.NutritionLog
import kotlinx.coroutines.flow.Flow

@Dao
interface NutritionLogDao {
    @Query("SELECT * FROM nutrition_logs WHERE dateKey = :dateKey LIMIT 1")
    fun getNutritionForDate(dateKey: String): Flow<NutritionLog?>

    @Query("SELECT * FROM nutrition_logs ORDER BY dateKey DESC LIMIT 30")
    fun getRecentNutritionLogs(): Flow<List<NutritionLog>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertOrUpdate(nutritionLog: NutritionLog)

    @Query("SELECT * FROM nutrition_logs WHERE dateKey = :dateKey LIMIT 1")
    suspend fun getNutritionSync(dateKey: String): NutritionLog?
}
