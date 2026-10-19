package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.local.AppDatabase
import com.example.data.model.Badge
import com.example.data.model.CustomRoutineEntity
import com.example.data.model.Exercise
import com.example.data.model.HeightLog
import com.example.data.model.NutritionLog
import com.example.data.model.PredefinedExercises
import com.example.data.model.PredefinedRoutines
import com.example.data.model.SleepLog
import com.example.data.model.WorkoutRoutine
import com.example.data.model.WorkoutSessionLog
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

class FitnessRepository(context: Context) {
    private val db = AppDatabase.getDatabase(context)
    private val heightDao = db.heightLogDao()
    private val nutritionDao = db.nutritionLogDao()
    private val sleepDao = db.sleepLogDao()
    private val workoutDao = db.workoutLogDao()
    private val customRoutineDao = db.customRoutineDao()

    private val prefs: SharedPreferences =
        context.getSharedPreferences("posture_grow_prefs", Context.MODE_PRIVATE)

    companion object {
        fun getTodayKey(): String {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(Date())
        }

        fun getPastDateKey(daysAgo: Int): String {
            val cal = Calendar.getInstance()
            cal.add(Calendar.DAY_OF_YEAR, -daysAgo)
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            return sdf.format(cal.time)
        }
    }

    // Heights
    val heightLogsAsc: Flow<List<HeightLog>> = heightDao.getAllHeightLogsAsc()
    val heightLogsDesc: Flow<List<HeightLog>> = heightDao.getAllHeightLogsDesc()
    val latestHeightLog: Flow<HeightLog?> = heightDao.getLatestHeightLog()

    suspend fun addHeightLog(heightCm: Float, weightKg: Float = 65f, note: String = "") {
        heightDao.insert(HeightLog(heightCm = heightCm, weightKg = weightKg, note = note))
    }

    suspend fun deleteHeightLog(id: Long) {
        heightDao.deleteById(id)
    }

    // Nutrition
    fun getNutritionForDate(dateKey: String): Flow<NutritionLog?> =
        nutritionDao.getNutritionForDate(dateKey)

    val recentNutritionLogs: Flow<List<NutritionLog>> =
        nutritionDao.getRecentNutritionLogs()

    suspend fun updateNutrition(
        dateKey: String,
        calciumDelta: Int = 0,
        vitDDelta: Int = 0,
        proteinDelta: Int = 0,
        waterDelta: Int = 0
    ) {
        val existing = nutritionDao.getNutritionSync(dateKey) ?: NutritionLog(dateKey = dateKey)
        val updated = existing.copy(
            calciumMg = (existing.calciumMg + calciumDelta).coerceAtLeast(0),
            vitaminDIu = (existing.vitaminDIu + vitDDelta).coerceAtLeast(0),
            proteinG = (existing.proteinG + proteinDelta).coerceAtLeast(0),
            waterMl = (existing.waterMl + waterDelta).coerceAtLeast(0)
        )
        nutritionDao.insertOrUpdate(updated)
    }

    suspend fun setNutritionDirect(nutritionLog: NutritionLog) {
        nutritionDao.insertOrUpdate(nutritionLog)
    }

    // Sleep & HGH
    fun getSleepForDate(dateKey: String): Flow<SleepLog?> =
        sleepDao.getSleepForDate(dateKey)

    val recentSleepLogs: Flow<List<SleepLog>> =
        sleepDao.getRecentSleepLogs()

    suspend fun saveSleepLog(sleepLog: SleepLog) {
        sleepDao.insertOrUpdate(sleepLog)
    }

    // Workout sessions
    val allWorkoutSessions: Flow<List<WorkoutSessionLog>> =
        workoutDao.getAllWorkoutSessions()

    fun getWorkoutSessionsForDate(dateKey: String): Flow<List<WorkoutSessionLog>> =
        workoutDao.getSessionsForDate(dateKey)

    val totalCompletedWorkouts: Flow<Int> = workoutDao.getTotalCompletedCount()
    val totalActiveDays: Flow<Int> = workoutDao.getTotalActiveDays()

    suspend fun logCompletedWorkout(
        routineId: String,
        routineTitle: String,
        durationSeconds: Int,
        exercisesCompleted: Int
    ) {
        val today = getTodayKey()
        workoutDao.insertSession(
            WorkoutSessionLog(
                routineId = routineId,
                routineTitle = routineTitle,
                dateKey = today,
                durationSeconds = durationSeconds,
                exercisesCompleted = exercisesCompleted
            )
        )
        updateStreak()
    }

    // Custom Routines
    val customRoutines: Flow<List<WorkoutRoutine>> =
        customRoutineDao.getAllCustomRoutines().map { entities ->
            entities.map { entity ->
                val exerciseIds = entity.exerciseIdsCsv.split(",").map { it.trim() }
                val exercises = exerciseIds.mapNotNull { PredefinedExercises.findById(it) }
                WorkoutRoutine(
                    id = entity.id,
                    title = entity.title,
                    subtitle = "Custom Routine • ${exercises.size} Exercises",
                    description = entity.description,
                    exercises = exercises,
                    defaultSecondsPerExercise = entity.secondsPerExercise,
                    accentColorHex = 0xFFFF4081,
                    isCustom = true
                )
            }
        }

    suspend fun saveCustomRoutine(
        title: String,
        description: String,
        exercises: List<Exercise>,
        secondsPerExercise: Int = 30
    ) {
        val id = "custom_" + System.currentTimeMillis()
        val csv = exercises.joinToString(",") { it.id }
        customRoutineDao.insertOrUpdate(
            CustomRoutineEntity(
                id = id,
                title = title,
                description = description,
                exerciseIdsCsv = csv,
                secondsPerExercise = secondsPerExercise
            )
        )
    }

    suspend fun deleteCustomRoutine(id: String) {
        customRoutineDao.deleteById(id)
    }

    // Preferences & Streak Engine
    fun getCurrentStreak(): Int = prefs.getInt("current_streak", 1)
    fun getLongestStreak(): Int = prefs.getInt("longest_streak", 1)
    fun getTargetHeightCm(): Float = prefs.getFloat("target_height_cm", 178.0f)
    fun setTargetHeightCm(value: Float) = prefs.edit().putFloat("target_height_cm", value).apply()
    fun isMetric(): Boolean = prefs.getBoolean("is_metric", true)
    fun setMetric(value: Boolean) = prefs.edit().putBoolean("is_metric", value).apply()

    private fun updateStreak() {
        val today = getTodayKey()
        val lastDate = prefs.getString("last_workout_date", "") ?: ""
        if (lastDate == today) {
            return
        }

        val yesterday = getPastDateKey(1)
        var current = prefs.getInt("current_streak", 0)
        current = if (lastDate == yesterday) {
            current + 1
        } else {
            1
        }
        val longest = maxOf(current, prefs.getInt("longest_streak", 1))
        prefs.edit()
            .putString("last_workout_date", today)
            .putInt("current_streak", current)
            .putInt("longest_streak", longest)
            .apply()
    }

    // Gamification Badges Flow
    val badgesFlow: Flow<List<Badge>> = combine(
        workoutDao.getTotalCompletedCount(),
        workoutDao.getTotalActiveDays(),
        heightDao.getAllHeightLogsAsc(),
        sleepDao.getHghOptimalWindowCount()
    ) { totalCompleted, activeDays, heightLogs, hghCount ->
        listOf(
            Badge(
                id = "first_stretch",
                title = "First Stretch",
                description = "Complete your first spine elongation circuit",
                iconName = "check_circle",
                isUnlocked = totalCompleted >= 1,
                progress = (totalCompleted.toFloat() / 1f).coerceIn(0f, 1f),
                progressLabel = if (totalCompleted >= 1) "Completed" else "0/1"
            ),
            Badge(
                id = "spine_aligner",
                title = "Spine Aligner",
                description = "Active on 3 separate days of posture correction",
                iconName = "airline_seat_recline_normal",
                isUnlocked = activeDays >= 3,
                progress = (activeDays.toFloat() / 3f).coerceIn(0f, 1f),
                progressLabel = "${minOf(activeDays, 3)}/3 Days"
            ),
            Badge(
                id = "growth_pioneer",
                title = "Growth Pioneer",
                description = "Reach 7 active days of height conditioning",
                iconName = "local_fire_department",
                isUnlocked = activeDays >= 7,
                progress = (activeDays.toFloat() / 7f).coerceIn(0f, 1f),
                progressLabel = "${minOf(activeDays, 7)}/7 Days"
            ),
            Badge(
                id = "decompression_guru",
                title = "Decompression Master",
                description = "Complete 10 total decompression sessions",
                iconName = "fitness_center",
                isUnlocked = totalCompleted >= 10,
                progress = (totalCompleted.toFloat() / 10f).coerceIn(0f, 1f),
                progressLabel = "${minOf(totalCompleted, 10)}/10"
            ),
            Badge(
                id = "hgh_optimizer",
                title = "HGH Optimizer",
                description = "Log sleep inside the optimal 10 PM - 2 AM window",
                iconName = "bedtime",
                isUnlocked = hghCount >= 3,
                progress = (hghCount.toFloat() / 3f).coerceIn(0f, 1f),
                progressLabel = "${minOf(hghCount, 3)}/3 Logs"
            ),
            Badge(
                id = "centimeter_club",
                title = "Milestone Tracker",
                description = "Log at least 3 progressive height checkpoints",
                iconName = "trending_up",
                isUnlocked = heightLogs.size >= 3,
                progress = (heightLogs.size.toFloat() / 3f).coerceIn(0f, 1f),
                progressLabel = "${minOf(heightLogs.size, 3)}/3 Logs"
            )
        )
    }

    // Seed realistic starter data on first run
    suspend fun seedInitialDataIfNeeded() {
        val seeded = prefs.getBoolean("data_seeded_v1", false)
        if (!seeded) {
            // Seed progressive height baseline entries to demonstrate the line chart
            val now = System.currentTimeMillis()
            val dayMillis = 86400000L
            heightDao.insert(
                HeightLog(
                    heightCm = 173.2f,
                    weightKg = 66.5f,
                    timestamp = now - (28 * dayMillis),
                    note = "Initial Posture Baseline Assessment"
                )
            )
            heightDao.insert(
                HeightLog(
                    heightCm = 173.8f,
                    weightKg = 66.2f,
                    timestamp = now - (14 * dayMillis),
                    note = "2 Weeks: Thoracic extension improved"
                )
            )
            heightDao.insert(
                HeightLog(
                    heightCm = 174.5f,
                    weightKg = 66.0f,
                    timestamp = now - (2 * dayMillis),
                    note = "4 Weeks: Decompressed disc gains + Pelvic tilt fixed"
                )
            )

            // Seed today's and yesterday's nutrition
            val today = getTodayKey()
            val yesterday = getPastDateKey(1)
            nutritionDao.insertOrUpdate(
                NutritionLog(
                    dateKey = today,
                    calciumMg = 950,
                    vitaminDIu = 600,
                    proteinG = 65,
                    waterMl = 2000
                )
            )
            nutritionDao.insertOrUpdate(
                NutritionLog(
                    dateKey = yesterday,
                    calciumMg = 1200,
                    vitaminDIu = 800,
                    proteinG = 82,
                    waterMl = 2600
                )
            )

            // Seed sleep logs
            sleepDao.insertOrUpdate(
                SleepLog(
                    dateKey = today,
                    hoursSlept = 8.2f,
                    bedtime = "22:15",
                    wakeTime = "06:30",
                    qualityRating = 5,
                    sleptDuringHghWindow = true,
                    deepSleepHours = 2.4f,
                    notes = "Asleep during prime 10PM-2AM HGH burst window"
                )
            )
            sleepDao.insertOrUpdate(
                SleepLog(
                    dateKey = yesterday,
                    hoursSlept = 7.8f,
                    bedtime = "22:45",
                    wakeTime = "06:35",
                    qualityRating = 4,
                    sleptDuringHghWindow = true,
                    deepSleepHours = 2.1f,
                    notes = "Good deep sleep recovery after bedtime decompression"
                )
            )

            // Seed completed session
            workoutDao.insertSession(
                WorkoutSessionLog(
                    routineId = PredefinedRoutines.SPINE_DECOMPRESSION.id,
                    routineTitle = PredefinedRoutines.SPINE_DECOMPRESSION.title,
                    dateKey = yesterday,
                    timestamp = now - dayMillis,
                    durationSeconds = 120,
                    exercisesCompleted = 4
                )
            )

            prefs.edit()
                .putBoolean("data_seeded_v1", true)
                .putInt("current_streak", 3)
                .putInt("longest_streak", 5)
                .putString("last_workout_date", yesterday)
                .apply()
        }
    }
}
