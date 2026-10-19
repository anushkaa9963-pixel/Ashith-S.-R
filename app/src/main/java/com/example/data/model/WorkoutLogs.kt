package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "workout_session_logs")
data class WorkoutSessionLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val routineId: String,
    val routineTitle: String,
    val dateKey: String, // "yyyy-MM-dd"
    val timestamp: Long = System.currentTimeMillis(),
    val durationSeconds: Int,
    val exercisesCompleted: Int
)

@Entity(tableName = "custom_routines")
data class CustomRoutineEntity(
    @PrimaryKey
    val id: String,
    val title: String,
    val description: String,
    val exerciseIdsCsv: String,
    val secondsPerExercise: Int = 30,
    val createdAt: Long = System.currentTimeMillis()
)
