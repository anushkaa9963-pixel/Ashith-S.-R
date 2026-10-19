package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "height_logs")
data class HeightLog(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val heightCm: Float,
    val weightKg: Float = 65f,
    val timestamp: Long = System.currentTimeMillis(),
    val note: String = ""
)
