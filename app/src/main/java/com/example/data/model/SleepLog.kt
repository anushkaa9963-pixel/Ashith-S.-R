package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sleep_logs")
data class SleepLog(
    @PrimaryKey
    val dateKey: String, // "yyyy-MM-dd"
    val hoursSlept: Float,
    val bedtime: String = "22:30",
    val wakeTime: String = "06:30",
    val qualityRating: Int = 4, // 1 to 5 stars
    val sleptDuringHghWindow: Boolean = true, // 10 PM - 2 AM
    val deepSleepHours: Float = 2.0f,
    val notes: String = ""
)
