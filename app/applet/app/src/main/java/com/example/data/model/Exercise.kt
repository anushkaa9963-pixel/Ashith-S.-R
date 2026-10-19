package com.example.data.model

enum class ExerciseCategory(val displayName: String) {
    STRETCH("Spine Stretch"),
    DECOMPRESSION("Decompression"),
    WORKOUT("Dynamic Workout"),
    POSTURE("Posture Growth")
}

data class Exercise(
    val id: String,
    val name: String,
    val category: ExerciseCategory,
    val durationSeconds: Int = 45,
    val targetReps: String = "45 sec hold",
    val description: String,
    val spineBenefit: String,
    val instructions: List<String>,
    val targetMuscles: String,
    val imageResName: String
)

object ExerciseRepository {
    val sampleExercises = listOf(
        Exercise(
            id = "cobra_stretch",
            name = "Cobra Spine Stretch",
            category = ExerciseCategory.STRETCH,
            durationSeconds = 40,
            targetReps = "40 sec stretch",
            description = "Gentle backward extension that opens the thoracic spine and relieves intervertebral compression.",
            spineBenefit = "Decompresses lower lumbar discs and opens anterior chest posture.",
            instructions = listOf(
                "Lie face down on the mat with hands directly beneath shoulders.",
                "Inhale slowly and press your palms firmly to lift your chest off the mat.",
                "Keep hips grounded and shoulders relaxed away from your ears.",
                "Hold the peak elongation, feeling the spine lengthen vertebrae by vertebrae.",
                "Exhale gently and lower with control."
            ),
            targetMuscles = "Lumbar Erector, Abdominals, Anterior Chest",
            imageResName = "img_stretching"
        ),
        Exercise(
            id = "bar_hanging",
            name = "Overhead Bar Hanging",
            category = ExerciseCategory.DECOMPRESSION,
            durationSeconds = 45,
            targetReps = "45 sec hang",
            description = "Full-body vertical decompression utilizing gravity to maximize disc hydration and spinal expansion.",
            spineBenefit = "Relieves up to 5mm of cumulative daytime gravity compaction.",
            instructions = listOf(
                "Grip the pull-up bar firmly with hands slightly wider than shoulder-width.",
                "Gently hang freely with feet clearing the floor.",
                "Relax your core and allow your torso to elongate with gravity.",
                "Breathe rhythmically and avoid swinging or tense neck posture.",
                "Lower feet gently to dismount."
            ),
            targetMuscles = "Latissimus Dorsi, Spine Intervertebral Discs, Forearms",
            imageResName = "img_hanging"
        ),
        Exercise(
            id = "squat_jumps",
            name = "Explosive Squat Jumps",
            category = ExerciseCategory.WORKOUT,
            durationSeconds = 30,
            targetReps = "15 reps / 30 sec",
            description = "Stimulates human growth factors, knee cartilage remodeling, and explosive leg bone bone mineral density.",
            spineBenefit = "Spurs biological growth stimulation through micro-impact osteo-loading.",
            instructions = listOf(
                "Stand with feet shoulder-width apart in athletic ready stance.",
                "Hinge hips back and descend into a controlled quarter squat.",
                "Explosively extend through ankles, knees, and hips, launching vertically.",
                "Reach arms upward toward the ceiling at apex.",
                "Land softly on the balls of your feet and absorb smoothly into the next rep."
            ),
            targetMuscles = "Quadriceps, Glutes, Calves, Core Stabilizers",
            imageResName = "img_workout"
        ),
        Exercise(
            id = "tall_posture_stretch",
            name = "Standing Posture Reach",
            category = ExerciseCategory.POSTURE,
            durationSeconds = 45,
            targetReps = "45 sec reach",
            description = "Full body axial elongation targeting spinal alignment and upright ergonomic height posture.",
            spineBenefit = "Restores natural spinal curves and corrects forward slouching.",
            instructions = listOf(
                "Stand tall with feet hip-width apart and knees unlocked.",
                "Interlock fingers and reach arms high overhead with palms upward.",
                "Lengthen through the crown of your head while rooting feet down.",
                "Tuck chin slightly to align cervical and thoracic spine.",
                "Maintain deep rhythmic diaphragmatic breathing."
            ),
            targetMuscles = "Thoracic Extensors, Shoulders, Cervical Alignment",
            imageResName = "img_posture"
        )
    )

    fun getExerciseById(id: String): Exercise {
        return sampleExercises.find { it.id == id } ?: sampleExercises.first()
    }
}
