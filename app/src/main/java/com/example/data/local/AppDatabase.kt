package com.example.data.local

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.data.model.CustomRoutineEntity
import com.example.data.model.HeightLog
import com.example.data.model.NutritionLog
import com.example.data.model.SleepLog
import com.example.data.model.WorkoutSessionLog

@Database(
    entities = [
        HeightLog::class,
        NutritionLog::class,
        SleepLog::class,
        WorkoutSessionLog::class,
        CustomRoutineEntity::class
    ],
    version = 1,
    exportSchema = false
)
abstract class AppDatabase : RoomDatabase() {
    abstract fun heightLogDao(): HeightLogDao
    abstract fun nutritionLogDao(): NutritionLogDao
    abstract fun sleepLogDao(): SleepLogDao
    abstract fun workoutLogDao(): WorkoutLogDao
    abstract fun customRoutineDao(): CustomRoutineDao

    companion object {
        @Volatile
        private var INSTANCE: AppDatabase? = null

        fun getDatabase(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    "posture_grow_database"
                )
                    .fallbackToDestructiveMigration()
                    .build()
                INSTANCE = instance
                instance
            }
        }
    }
}
