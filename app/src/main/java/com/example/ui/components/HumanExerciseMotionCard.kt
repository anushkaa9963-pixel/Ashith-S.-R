package com.example.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material.icons.filled.Straighten
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonOrange
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

/**
 * Returns the corresponding photo asset showing a human in shorts & t-shirt
 * performing the specific stretch or workout.
 */
fun getExerciseHumanImage(exerciseId: String, category: ExerciseCategory): Int {
    return when (exerciseId) {
        "cobra_stretch", "cat_cow_stretch", "childs_pose", "bird_dog", "pelvic_tilts" -> {
            R.drawable.img_human_stretching
        }
        "hanging_bar" -> {
            R.drawable.img_human_hanging
        }
        "jump_squats", "rope_skipping", "ankle_weights_leg_lifts" -> {
            R.drawable.img_human_workout
        }
        "forward_fold", "wall_sits", "calf_raises" -> {
            R.drawable.img_human_posture_stretch
        }
        else -> when (category) {
            ExerciseCategory.SPINE_DECOMPRESSION -> R.drawable.img_human_stretching
            ExerciseCategory.POSTURE_ALIGNMENT -> R.drawable.img_human_posture_stretch
            ExerciseCategory.LEG_LENGTHENING -> R.drawable.img_human_workout
            ExerciseCategory.CUSTOM -> R.drawable.img_human_posture_stretch
        }
    }
}

/**
 * Displays a realistic photograph of a human wearing shorts and t-shirt
 * with real-time biomechanical stretching and exercise animation overlays.
 * Takes up approximately half of the viewport height as requested.
 */
@Composable
fun HumanExerciseMotionCard(
    exercise: Exercise,
    isPlaying: Boolean,
    remainingSeconds: Int,
    totalSeconds: Int,
    modifier: Modifier = Modifier,
    heightDp: Int = 320
) {
    val imageRes = getExerciseHumanImage(exercise.id, exercise.category)

    // Smooth continuous biomechanical animation transition
    val infiniteTransition = rememberInfiniteTransition(label = "human_motion_cycle")

    // Rhythmic stretch & breathing cycle (3.2 seconds loop)
    val stretchCycle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2800, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "stretch_expansion"
    )

    // Spinal pulse wave that moves upward along the spine
    val spinePulseOffset by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spine_pulse"
    )

    // Dynamic elongation values when playing
    val scaleY = if (isPlaying) 1f + (stretchCycle * 0.045f) else 1f
    val scaleX = if (isPlaying) 1f + (stretchCycle * 0.02f) else 1f
    val translateY = if (isPlaying) -stretchCycle * 10f else 0f

    // Exercise phase calculation based on loop cycle
    val cyclePhase = (totalSeconds - remainingSeconds) % 6
    val (phaseTitle, phaseColor) = when {
        !isPlaying -> "PAUSED • RESUME TO CONTINUE" to TextMuted
        cyclePhase < 2 -> "⬆️ INHALE • EXTEND & LENGTHEN" to NeonCyan
        cyclePhase < 4 -> "✨ PEAK STRETCH • HOLD ALIGNMENT" to NeonLime
        else -> "⬇️ EXHALE • CONTROLLED RELEASE" to NeonOrange
    }

    Box(
        modifier = modifier
            .fillMaxWidth()
            .height(heightDp.dp)
            .shadow(12.dp, RoundedCornerShape(20.dp))
            .clip(RoundedCornerShape(20.dp))
            .background(DarkSurface)
            .border(1.5.dp, if (isPlaying) NeonCyan.copy(alpha = 0.6f) else DarkCardBorder, RoundedCornerShape(20.dp))
            .testTag("human_exercise_motion_card")
    ) {
        // 1. The Human Photograph with dynamic stretching motion
        Image(
            painter = painterResource(id = imageRes),
            contentDescription = "Human athlete in shorts and t-shirt demonstrating ${exercise.name}",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxSize()
                .graphicsLayer {
                    this.scaleY = scaleY
                    this.scaleX = scaleX
                    this.translationY = translateY
                }
        )

        // 2. Cinematic athletic gradient overlay (vignette for readability)
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(
                    Brush.verticalGradient(
                        colors = listOf(
                            DarkBackground.copy(alpha = 0.75f),
                            Color.Transparent,
                            DarkBackground.copy(alpha = 0.90f)
                        ),
                        startY = 0f,
                        endY = Float.POSITIVE_INFINITY
                    )
                )
        )

        // 3. Biomechanical Spinal Decompression & Alignment Overlay Canvas
        Canvas(modifier = Modifier.fillMaxSize()) {
            val width = size.width
            val height = size.height

            if (isPlaying) {
                // Animated spinal elongation kinetic line
                val startX = width * 0.5f
                val startY = height * 0.75f
                val endY = height * 0.28f - (stretchCycle * 18f)

                // Spine guide trace
                drawLine(
                    color = NeonCyan.copy(alpha = 0.35f),
                    start = Offset(startX, startY),
                    end = Offset(startX, endY),
                    strokeWidth = 3.dp.toPx(),
                    cap = StrokeCap.Round
                )

                // Vertebral nodes expanding with the stretch
                val nodeCount = 5
                val totalLength = startY - endY
                for (i in 0 until nodeCount) {
                    val progress = i.toFloat() / (nodeCount - 1)
                    // Expand vertebral gap dynamically
                    val nodeY = startY - (progress * totalLength)
                    val isLeadingNode = i == nodeCount - 1

                    drawCircle(
                        color = if (isLeadingNode) NeonLime else NeonCyan,
                        radius = (if (isLeadingNode) 5.dp else 3.5.dp).toPx(),
                        center = Offset(startX, nodeY)
                    )
                }

                // Pulsing kinetic wave packet moving upwards
                val waveY = startY - (spinePulseOffset * totalLength)
                drawCircle(
                    color = NeonLime.copy(alpha = 0.85f),
                    radius = 7.dp.toPx(),
                    center = Offset(startX, waveY)
                )
                drawCircle(
                    color = NeonCyan.copy(alpha = 0.3f),
                    radius = 14.dp.toPx(),
                    center = Offset(startX, waveY)
                )
            }
        }

        // 4. Top Overlay HUD: Form Guide Badge & Clothing Info
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(12.dp)
                .align(Alignment.TopCenter),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Left Chip: Human Demonstration
            Box(
                modifier = Modifier
                    .background(DarkBackground.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
                    .border(1.dp, NeonCyan.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "HUMAN FORM GUIDE",
                        color = TextPrimary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Black,
                        letterSpacing = 0.8.sp
                    )
                }
            }

            // Right Chip: Shorts & T-Shirt attire confirmation
            Box(
                modifier = Modifier
                    .background(DarkBackground.copy(alpha = 0.85f), RoundedCornerShape(12.dp))
                    .border(1.dp, NeonLime.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 10.dp, vertical = 5.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(7.dp)
                            .background(if (isPlaying) NeonLime else TextMuted, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "T-SHIRT & SHORTS",
                        color = NeonLime,
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        // 5. Bottom Overlay HUD: Live Motion Phase & Biomechanical Alignment Info
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
                .align(Alignment.BottomCenter)
        ) {
            // Dynamic Phase Indicator
            Box(
                modifier = Modifier
                    .background(DarkBackground.copy(alpha = 0.88f), RoundedCornerShape(10.dp))
                    .border(1.dp, phaseColor.copy(alpha = 0.5f), RoundedCornerShape(10.dp))
                    .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = phaseTitle,
                    color = phaseColor,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    letterSpacing = 0.6.sp
                )
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Exercise Title and Form Target
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.name,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Black
                    )
                    Text(
                        text = "Target: ${exercise.targetArea}",
                        color = TextSecondary,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Medium,
                        maxLines = 1
                    )
                }

                // Decompression elongation status
                Box(
                    modifier = Modifier
                        .background(NeonCyan.copy(alpha = 0.15f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Straighten,
                            contentDescription = null,
                            tint = NeonCyan,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isPlaying) "DECOMPRESSING" else "HOLD",
                            color = NeonCyan,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}
