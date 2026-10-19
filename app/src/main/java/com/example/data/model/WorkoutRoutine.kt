package com.example.data.model

data class WorkoutRoutine(
    val id: String,
    val title: String,
    val subtitle: String,
    val description: String,
    val exercises: List<Exercise>,
    val defaultSecondsPerExercise: Int = 30,
    val accentColorHex: Long = 0xFF00E5FF,
    val isCustom: Boolean = false,
    val isMorningRoutine: Boolean = false,
    val isBedtimeRoutine: Boolean = false
) {
    val totalDurationSeconds: Int
        get() = exercises.size * defaultSecondsPerExercise
}

object PredefinedRoutines {
    val SPINE_DECOMPRESSION = WorkoutRoutine(
        id = "routine_spine_decompression",
        title = "Spine Decompression Circuit",
        subtitle = "Maximize Vertebral Extension",
        description = "4 core spine-elongating exercises designed to relieve intervertebral pressure and lengthen spinal discs.",
        exercises = listOf(
            PredefinedExercises.HANGING,
            PredefinedExercises.COBRA_STRETCH,
            PredefinedExercises.FORWARD_FOLD,
            PredefinedExercises.CAT_COW_STRETCH
        ),
        defaultSecondsPerExercise = 30,
        accentColorHex = 0xFF00E5FF
    )

    val POSTURE_ALIGNMENT = WorkoutRoutine(
        id = "routine_posture_alignment",
        title = "Posture Correction & Alignment",
        subtitle = "Fix Slouch & Pelvic Tilt",
        description = "Eliminates rounded shoulders, text neck, and anterior pelvic tilt to unlock 1-3 cm of hidden posture height.",
        exercises = listOf(
            PredefinedExercises.WALL_SITS,
            PredefinedExercises.CHILDS_POSE,
            PredefinedExercises.BIRD_DOG,
            PredefinedExercises.PELVIC_TILTS
        ),
        defaultSecondsPerExercise = 30,
        accentColorHex = 0xFF00E676
    )

    val LEG_LENGTHENING = WorkoutRoutine(
        id = "routine_leg_lengthening",
        title = "Lower Body Leg-Lengthening",
        subtitle = "Stimulate Bone Density & Growth Zones",
        description = "High-efficiency kinetic loading routine targeting tibia, femur, and epiphyseal remodeling.",
        exercises = listOf(
            PredefinedExercises.ANKLE_WEIGHTS_LEG_LIFTS,
            PredefinedExercises.JUMP_SQUATS,
            PredefinedExercises.ROPE_SKIPPING,
            PredefinedExercises.CALF_RAISES
        ),
        defaultSecondsPerExercise = 30,
        accentColorHex = 0xFFFFD600
    )

    val MORNING_WAKEUP = WorkoutRoutine(
        id = "routine_morning_wakeup",
        title = "Morning Wake-Up Stretches",
        subtitle = "AM Spinal Release & Posture Reset",
        description = "Optimal wake-up protocol to reverse night-time spinal stiffness, awaken stabilizing muscles, and set upright posture for the day.",
        exercises = listOf(
            PredefinedExercises.CAT_COW_STRETCH,
            PredefinedExercises.COBRA_STRETCH,
            PredefinedExercises.FORWARD_FOLD,
            PredefinedExercises.WALL_SITS,
            PredefinedExercises.CALF_RAISES
        ),
        defaultSecondsPerExercise = 30,
        accentColorHex = 0xFFFF9100,
        isMorningRoutine = true
    )

    val BEDTIME_DECOMPRESSION = WorkoutRoutine(
        id = "routine_bedtime_decompression",
        title = "Before-Bed Decompression",
        subtitle = "PM Disc Rehydration & HGH Prep",
        description = "Evening spinal unloading protocol to decompress discs before sleep, optimizing nocturnal Human Growth Hormone release.",
        exercises = listOf(
            PredefinedExercises.HANGING,
            PredefinedExercises.CHILDS_POSE,
            PredefinedExercises.PELVIC_TILTS,
            PredefinedExercises.BIRD_DOG,
            PredefinedExercises.FORWARD_FOLD
        ),
        defaultSecondsPerExercise = 30,
        accentColorHex = 0xFF7C4DFF,
        isBedtimeRoutine = true
    )

    val ALL_PREDEFINED = listOf(
        SPINE_DECOMPRESSION,
        POSTURE_ALIGNMENT,
        LEG_LENGTHENING,
        MORNING_WAKEUP,
        BEDTIME_DECOMPRESSION
    )
}
