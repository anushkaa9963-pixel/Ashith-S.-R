package com.example.ui

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.model.Badge
import com.example.data.model.Exercise
import com.example.data.model.FitnessQuotes
import com.example.data.model.HeightLog
import com.example.data.model.MotivationalQuote
import com.example.data.model.NutritionLog
import com.example.data.model.PredefinedExercises
import com.example.data.model.PredefinedRoutines
import com.example.data.model.SleepLog
import com.example.data.model.WorkoutRoutine
import com.example.data.model.WorkoutSessionLog
import com.example.data.repository.FitnessRepository
import com.example.ui.components.AudioBeepHelper
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

data class WorkoutPlayerState(
    val activeRoutine: WorkoutRoutine? = null,
    val currentExerciseIndex: Int = 0,
    val remainingSeconds: Int = 30,
    val totalExerciseSeconds: Int = 30,
    val isPlaying: Boolean = false,
    val isCompleted: Boolean = false,
    val totalElapsedTime: Int = 0
) {
    val currentExercise: Exercise?
        get() = activeRoutine?.exercises?.getOrNull(currentExerciseIndex)

    val nextExercise: Exercise?
        get() = activeRoutine?.exercises?.getOrNull(currentExerciseIndex + 1)

    val progressPercent: Float
        get() = if (totalExerciseSeconds > 0) {
            remainingSeconds.toFloat() / totalExerciseSeconds.toFloat()
        } else 0f
}

class MainViewModel(application: Application) : AndroidViewModel(application) {
    private val repository = FitnessRepository(application)
    private val beepHelper = AudioBeepHelper(application)

    val todayDateKey = FitnessRepository.getTodayKey()

    // Height & Progress
    val heightLogs: StateFlow<List<HeightLog>> = repository.heightLogsAsc
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val latestHeight: StateFlow<HeightLog?> = repository.latestHeightLog
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    private val _isMetric = MutableStateFlow(repository.isMetric())
    val isMetric: StateFlow<Boolean> = _isMetric.asStateFlow()

    private val _targetHeightCm = MutableStateFlow(repository.getTargetHeightCm())
    val targetHeightCm: StateFlow<Float> = _targetHeightCm.asStateFlow()

    // Nutrition
    val todayNutrition: StateFlow<NutritionLog?> = repository.getNutritionForDate(todayDateKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    // Sleep
    val todaySleep: StateFlow<SleepLog?> = repository.getSleepForDate(todayDateKey)
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), null)

    val recentSleepLogs: StateFlow<List<SleepLog>> = repository.recentSleepLogs
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    // Routines (Predefined + Custom)
    val customRoutines: StateFlow<List<WorkoutRoutine>> = repository.customRoutines
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val allRoutines: StateFlow<List<WorkoutRoutine>> = combine(
        customRoutines,
        _targetHeightCm
    ) { custom, _ ->
        PredefinedRoutines.ALL_PREDEFINED + custom
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), PredefinedRoutines.ALL_PREDEFINED)

    // Completed sessions
    val completedSessions: StateFlow<List<WorkoutSessionLog>> = repository.allWorkoutSessions
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    val totalCompletedWorkouts: StateFlow<Int> = repository.totalCompletedWorkouts
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), 0)

    // Badges & Gamification
    val badges: StateFlow<List<Badge>> = repository.badgesFlow
        .stateIn(viewModelScope, SharingStarted.WhileSubscribed(5000), emptyList())

    private val _currentStreak = MutableStateFlow(repository.getCurrentStreak())
    val currentStreak: StateFlow<Int> = _currentStreak.asStateFlow()

    private val _longestStreak = MutableStateFlow(repository.getLongestStreak())
    val longestStreak: StateFlow<Int> = _longestStreak.asStateFlow()

    private val _quoteOfTheDay = MutableStateFlow(FitnessQuotes.getRandomQuote())
    val quoteOfTheDay: StateFlow<MotivationalQuote> = _quoteOfTheDay.asStateFlow()

    // Workout Player State
    private val _playerState = MutableStateFlow(WorkoutPlayerState())
    val playerState: StateFlow<WorkoutPlayerState> = _playerState.asStateFlow()

    private var timerJob: Job? = null

    // Custom Routine Builder State
    private val _builderExercises = MutableStateFlow<List<Exercise>>(
        listOf(
            PredefinedExercises.HANGING,
            PredefinedExercises.COBRA_STRETCH,
            PredefinedExercises.PELVIC_TILTS
        )
    )
    val builderExercises: StateFlow<List<Exercise>> = _builderExercises.asStateFlow()

    init {
        viewModelScope.launch {
            repository.seedInitialDataIfNeeded()
            _currentStreak.value = repository.getCurrentStreak()
            _longestStreak.value = repository.getLongestStreak()
        }
    }

    // Workout Player Controls
    fun startRoutine(routine: WorkoutRoutine) {
        val firstExerciseDuration = routine.defaultSecondsPerExercise
        _playerState.value = WorkoutPlayerState(
            activeRoutine = routine,
            currentExerciseIndex = 0,
            remainingSeconds = firstExerciseDuration,
            totalExerciseSeconds = firstExerciseDuration,
            isPlaying = true,
            isCompleted = false,
            totalElapsedTime = 0
        )
        beepHelper.playTransitionBeep()
        startTimerLoop()
    }

    fun togglePlayPause() {
        val current = _playerState.value
        if (current.isCompleted) return

        if (current.isPlaying) {
            timerJob?.cancel()
            _playerState.value = current.copy(isPlaying = false)
        } else {
            _playerState.value = current.copy(isPlaying = true)
            startTimerLoop()
        }
    }

    private fun startTimerLoop() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (_playerState.value.isPlaying && !_playerState.value.isCompleted) {
                delay(1000L)
                val current = _playerState.value
                val remaining = current.remainingSeconds - 1

                if (remaining in 1..3) {
                    beepHelper.playCountdownBeep()
                }

                if (remaining <= 0) {
                    // Current exercise completed
                    val routine = current.activeRoutine ?: break
                    val nextIndex = current.currentExerciseIndex + 1

                    if (nextIndex < routine.exercises.size) {
                        // Move to next exercise
                        val nextSec = routine.defaultSecondsPerExercise
                        beepHelper.playTransitionBeep()
                        _playerState.value = current.copy(
                            currentExerciseIndex = nextIndex,
                            remainingSeconds = nextSec,
                            totalExerciseSeconds = nextSec,
                            totalElapsedTime = current.totalElapsedTime + current.totalExerciseSeconds
                        )
                    } else {
                        // Entire routine completed!
                        beepHelper.playWorkoutCompleteBeep()
                        val totalElapsed = current.totalElapsedTime + current.totalExerciseSeconds
                        _playerState.value = current.copy(
                            remainingSeconds = 0,
                            isPlaying = false,
                            isCompleted = true,
                            totalElapsedTime = totalElapsed
                        )

                        // Log to database
                        repository.logCompletedWorkout(
                            routineId = routine.id,
                            routineTitle = routine.title,
                            durationSeconds = totalElapsed,
                            exercisesCompleted = routine.exercises.size
                        )
                        _currentStreak.value = repository.getCurrentStreak()
                        _longestStreak.value = repository.getLongestStreak()
                        break
                    }
                } else {
                    _playerState.value = current.copy(remainingSeconds = remaining)
                }
            }
        }
    }

    fun skipToNextExercise() {
        val current = _playerState.value
        val routine = current.activeRoutine ?: return
        val nextIndex = current.currentExerciseIndex + 1

        if (nextIndex < routine.exercises.size) {
            beepHelper.playTransitionBeep()
            val nextSec = routine.defaultSecondsPerExercise
            _playerState.value = current.copy(
                currentExerciseIndex = nextIndex,
                remainingSeconds = nextSec,
                totalExerciseSeconds = nextSec
            )
        } else {
            finishWorkoutEarly()
        }
    }

    fun previousExercise() {
        val current = _playerState.value
        val routine = current.activeRoutine ?: return
        val prevIndex = (current.currentExerciseIndex - 1).coerceAtLeast(0)
        val sec = routine.defaultSecondsPerExercise
        _playerState.value = current.copy(
            currentExerciseIndex = prevIndex,
            remainingSeconds = sec,
            totalExerciseSeconds = sec
        )
    }

    fun restartCurrentExercise() {
        val current = _playerState.value
        val sec = current.totalExerciseSeconds
        _playerState.value = current.copy(remainingSeconds = sec)
    }

    fun finishWorkoutEarly() {
        timerJob?.cancel()
        val current = _playerState.value
        val routine = current.activeRoutine ?: return
        beepHelper.playWorkoutCompleteBeep()
        _playerState.value = current.copy(
            isPlaying = false,
            isCompleted = true
        )
        viewModelScope.launch {
            repository.logCompletedWorkout(
                routineId = routine.id,
                routineTitle = routine.title,
                durationSeconds = current.totalElapsedTime,
                exercisesCompleted = current.currentExerciseIndex + 1
            )
            _currentStreak.value = repository.getCurrentStreak()
            _longestStreak.value = repository.getLongestStreak()
        }
    }

    fun closeWorkoutPlayer() {
        timerJob?.cancel()
        _playerState.value = WorkoutPlayerState()
    }

    // Height Actions
    fun addHeightEntry(heightCm: Float, weightKg: Float, note: String) {
        viewModelScope.launch {
            repository.addHeightLog(heightCm = heightCm, weightKg = weightKg, note = note)
        }
    }

    fun toggleMetric() {
        val newMetric = !_isMetric.value
        _isMetric.value = newMetric
        repository.setMetric(newMetric)
    }

    fun updateTargetHeight(heightCm: Float) {
        _targetHeightCm.value = heightCm
        repository.setTargetHeightCm(heightCm)
    }

    // Nutrition Actions
    fun addNutritionIntake(calciumMg: Int = 0, vitDIu: Int = 0, proteinG: Int = 0, waterMl: Int = 0) {
        viewModelScope.launch {
            repository.updateNutrition(
                dateKey = todayDateKey,
                calciumDelta = calciumMg,
                vitDDelta = vitDIu,
                proteinDelta = proteinG,
                waterDelta = waterMl
            )
        }
    }

    // Sleep Actions
    fun logSleep(hours: Float, bedtime: String, wakeTime: String, quality: Int, inHghWindow: Boolean, notes: String = "") {
        viewModelScope.launch {
            repository.saveSleepLog(
                SleepLog(
                    dateKey = todayDateKey,
                    hoursSlept = hours,
                    bedtime = bedtime,
                    wakeTime = wakeTime,
                    qualityRating = quality,
                    sleptDuringHghWindow = inHghWindow,
                    deepSleepHours = (hours * 0.28f).coerceAtLeast(1.0f),
                    notes = notes
                )
            )
        }
    }

    // Custom Routine Builder Actions
    fun addExerciseToBuilder(exercise: Exercise) {
        _builderExercises.value = _builderExercises.value + exercise
    }

    fun removeExerciseFromBuilder(index: Int) {
        val current = _builderExercises.value.toMutableList()
        if (index in current.indices) {
            current.removeAt(index)
            _builderExercises.value = current
        }
    }

    fun moveExerciseInBuilder(fromIndex: Int, toIndex: Int) {
        val current = _builderExercises.value.toMutableList()
        if (fromIndex in current.indices && toIndex in current.indices) {
            val item = current.removeAt(fromIndex)
            current.add(toIndex, item)
            _builderExercises.value = current
        }
    }

    fun saveCustomRoutine(title: String, description: String, secondsPerExercise: Int) {
        val exercises = _builderExercises.value
        if (exercises.isEmpty()) return
        viewModelScope.launch {
            repository.saveCustomRoutine(
                title = title.ifBlank { "Personalized Spine Circuit" },
                description = description.ifBlank { "Customized posture & elongation routine." },
                exercises = exercises,
                secondsPerExercise = secondsPerExercise
            )
        }
    }

    fun deleteCustomRoutine(id: String) {
        viewModelScope.launch {
            repository.deleteCustomRoutine(id)
        }
    }

    override fun onCleared() {
        super.onCleared()
        timerJob?.cancel()
        beepHelper.release()
    }
}
