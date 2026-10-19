package com.example.data.model

data class Exercise(
    val id: String,
    val name: String,
    val category: ExerciseCategory,
    val defaultDurationSeconds: Int = 30,
    val targetArea: String,
    val instructions: String,
    val spineBenefit: String,
    val iconName: String = "accessibility"
)

enum class ExerciseCategory(val displayName: String) {
    SPINE_DECOMPRESSION("Spine Decompression"),
    POSTURE_ALIGNMENT("Posture Alignment"),
    LEG_LENGTHENING("Leg Lengthening"),
    CUSTOM("Custom Routine")
}

object PredefinedExercises {
    // 1. Spine Decompression & Stretching Circuit
    val HANGING = Exercise(
        id = "hanging_bar",
        name = "Hanging (from a bar)",
        category = ExerciseCategory.SPINE_DECOMPRESSION,
        defaultDurationSeconds = 30,
        targetArea = "Spine, Intervertebral Discs & Shoulders",
        instructions = "Grip an overhead bar shoulder-width apart. Relax your lower body and let gravity gently pull and decompress your intervertebral discs. Breathe slowly.",
        spineBenefit = "Relieves disc compression, widens vertebral gaps, and counters gravity compression.",
        iconName = "fitness_center"
    )

    val COBRA_STRETCH = Exercise(
        id = "cobra_stretch",
        name = "Cobra Stretch",
        category = ExerciseCategory.SPINE_DECOMPRESSION,
        defaultDurationSeconds = 30,
        targetArea = "Thoracic Spine, Chest & Abdominals",
        instructions = "Lie face down with palms near chest. Gently push up through your palms, extending the thoracic spine upward while keeping hips grounded.",
        spineBenefit = "Increases spinal extension flexibility, opens up tight chest muscles and elongates the torso.",
        iconName = "self_improvement"
    )

    val FORWARD_FOLD = Exercise(
        id = "forward_fold",
        name = "Forward Fold (Toe Touches)",
        category = ExerciseCategory.SPINE_DECOMPRESSION,
        defaultDurationSeconds = 30,
        targetArea = "Hamstrings, Calves & Lumbar Spine",
        instructions = "Stand tall, hinge forward from the hips with a straight back. Reach for your toes or floor while keeping legs straight. Hold and breathe deeply.",
        spineBenefit = "Lengthens posterior chain, releases lower back tightness, and increases posterior spinal spacing.",
        iconName = "accessibility_new"
    )

    val CAT_COW_STRETCH = Exercise(
        id = "cat_cow_stretch",
        name = "Cat-Cow Stretch",
        category = ExerciseCategory.SPINE_DECOMPRESSION,
        defaultDurationSeconds = 30,
        targetArea = "Entire Spine & Core",
        instructions = "On all fours: Inhale, arch your back down, lift head (Cow). Exhale, tuck tailbone, arch spine towards ceiling like a cat (Cat). Flow smoothly.",
        spineBenefit = "Mobilizes all 33 vertebrae, stimulates synovial fluid circulation, and improves spine elasticity.",
        iconName = "pets"
    )

    // 2. Posture Correction & Alignment Circuit
    val WALL_SITS = Exercise(
        id = "wall_sits",
        name = "Wall Sits",
        category = ExerciseCategory.POSTURE_ALIGNMENT,
        defaultDurationSeconds = 30,
        targetArea = "Quads, Glutes & Spinal Erectors",
        instructions = "Lean against a flat wall, slide down until thighs are parallel to the floor. Press your entire spine, upper back, and shoulders flush against the wall.",
        spineBenefit = "Corrects forward head carriage, reinforces neutral vertical spine alignment, and builds isometric posture endurance.",
        iconName = "airline_seat_recline_normal"
    )

    val CHILDS_POSE = Exercise(
        id = "childs_pose",
        name = "Child’s Pose",
        category = ExerciseCategory.POSTURE_ALIGNMENT,
        defaultDurationSeconds = 30,
        targetArea = "Lats, Lower Spine & Hips",
        instructions = "Kneel with big toes touching, sit back on heels. Extend arms forward flat on the mat and lower chest to the floor. Reach fingertips forward.",
        spineBenefit = "Relaxes deep spinal erector muscles, gently opens the sacral region, and elongates the thoracic spine.",
        iconName = "spa"
    )

    val BIRD_DOG = Exercise(
        id = "bird_dog",
        name = "Bird-Dog",
        category = ExerciseCategory.POSTURE_ALIGNMENT,
        defaultDurationSeconds = 30,
        targetArea = "Core, Multifidus & Posterior Oblique Sling",
        instructions = "Start on all fours. Simultaneously extend your right arm forward and left leg backward until parallel to the ground. Switch sides halfway.",
        spineBenefit = "Strengthens deep stabilizers around the spine, fixes pelvic rotation, and cures anterior pelvic tilt.",
        iconName = "directions_run"
    )

    val PELVIC_TILTS = Exercise(
        id = "pelvic_tilts",
        name = "Pelvic Tilts",
        category = ExerciseCategory.POSTURE_ALIGNMENT,
        defaultDurationSeconds = 30,
        targetArea = "Lower Back, Abs & Pelvic Floor",
        instructions = "Lie on back with knees bent. Contract abdominals to flatten your lower back completely against the floor, tilting the pelvis upward. Hold and release.",
        spineBenefit = "Directly combats anterior pelvic tilt, restoring natural lumbar lordosis and adding apparent vertical stature.",
        iconName = "airline_seat_flat"
    )

    // 3. Lower Body Leg-Lengthening Circuit
    val ANKLE_WEIGHTS_LEG_LIFTS = Exercise(
        id = "ankle_weights_leg_lifts",
        name = "Ankle Weights Leg Lifts",
        category = ExerciseCategory.LEG_LENGTHENING,
        defaultDurationSeconds = 30,
        targetArea = "Hip Flexors, Quadriceps & Lower Tendons",
        instructions = "Lying flat or hanging, perform controlled vertical leg extensions. Focus on keeping knees straight to apply tension through the femur and tibia.",
        spineBenefit = "Enhances knee joint and micro-tendon extension while stimulating lower limb bone mineral loading.",
        iconName = "fitness_center"
    )

    val JUMP_SQUATS = Exercise(
        id = "jump_squats",
        name = "Jump Squats",
        category = ExerciseCategory.LEG_LENGTHENING,
        defaultDurationSeconds = 30,
        targetArea = "Femur, Tibia, Glutes & Epiphyseal Zones",
        instructions = "Squat down with chest high, then explosively jump vertically reaching your hands toward the ceiling. Land softly and repeat fluidly.",
        spineBenefit = "Generates high mechanical impact force on long bones, stimulating bone density and micro-fracture remodeling.",
        iconName = "trending_up"
    )

    val ROPE_SKIPPING = Exercise(
        id = "rope_skipping",
        name = "Rope Skipping",
        category = ExerciseCategory.LEG_LENGTHENING,
        defaultDurationSeconds = 30,
        targetArea = "Calves, Achilles Tendons & Growth Zones",
        instructions = "Bounce lightly on the balls of your feet in rhythm. Keep core tight and spine erect. Spring upward with minimal knee bend.",
        spineBenefit = "Rhythmic vertical impulses trigger growth hormone secretion and increase lower leg elasticity.",
        iconName = "cached"
    )

    val CALF_RAISES = Exercise(
        id = "calf_raises",
        name = "Calf Raises",
        category = ExerciseCategory.LEG_LENGTHENING,
        defaultDurationSeconds = 30,
        targetArea = "Gastrocnemius, Soleus & Tibial Tendons",
        instructions = "Stand on edge of step with heels hanging down. Lower heels below step level for a deep calf stretch, then press high onto toes.",
        spineBenefit = "Maximizes calf extension, promotes tendon length, and improves upright vertical kinetic chain.",
        iconName = "vertical_align_top"
    )

    val ALL: List<Exercise> = listOf(
        HANGING,
        COBRA_STRETCH,
        FORWARD_FOLD,
        CAT_COW_STRETCH,
        WALL_SITS,
        CHILDS_POSE,
        BIRD_DOG,
        PELVIC_TILTS,
        ANKLE_WEIGHTS_LEG_LIFTS,
        JUMP_SQUATS,
        ROPE_SKIPPING,
        CALF_RAISES
    )

    fun findById(id: String): Exercise? = ALL.find { it.id == id }
}
