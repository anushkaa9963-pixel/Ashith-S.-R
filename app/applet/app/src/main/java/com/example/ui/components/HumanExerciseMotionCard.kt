package com.example.ui.components

import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
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
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.North
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
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
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

fun getExerciseHumanImage(exerciseId: String, category: ExerciseCategory): Int {
    return when (exerciseId) {
        "cobra_stretch" -> R.drawable.img_stretching
        "bar_hanging" -> R.drawable.img_hanging
        "squat_jumps" -> R.drawable.img_workout
        "tall_posture_stretch" -> R.drawable.img_posture
        else -> when (category) {
            ExerciseCategory.STRETCH -> R.drawable.img_stretching
            ExerciseCategory.DECOMPRESSION -> R.drawable.img_hanging
            ExerciseCategory.WORKOUT -> R.drawable.img_workout
            ExerciseCategory.POSTURE -> R.drawable.img_posture
        }
    }
}

@Composable
fun HumanExerciseMotionCard(
    exercise: Exercise,
    isPlaying: Boolean,
    remainingSeconds: Int,
    modifier: Modifier = Modifier
) {
    val infiniteTransition = rememberInfiniteTransition(label = "humanMotion")

    // Vertical elongation stretch cycle
    val stretchPulse by infiniteTransition.animateFloat(
        initialValue = 0.98f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 2400, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "stretchPulse"
    )

    // Glowing spinal wave animation
    val spineGlowProgress by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1800, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "spineWave"
    )

    // Breathing phase: 0..1 Inhale/Expand, 1..2 Hold, 2..3 Exhale
    val breathCycle = (remainingSeconds % 6)
    val breathPrompt = when {
        !isPlaying -> "PAUSED - TAP RESUME"
        breathCycle < 2 -> "INHALE & ELONGATE SPINE"
        breathCycle < 4 -> "HOLD PEAK DECOMPRESSION"
        else -> "EXHALE & RELEASE TENSION"
    }

    val imageRes = getExerciseHumanImage(exercise.id, exercise.category)

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("human_exercise_motion_card"),
        shape = RoundedCornerShape(22.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 8.dp)
    ) {
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxWidth()
                .height(310.dp) // Takes roughly half the standard mobile viewport
        ) {
            // Background human photo with subtle stretch pulse animation
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .graphicsLayer {
                        if (isPlaying) {
                            scaleY = stretchPulse
                            scaleX = 1f + (stretchPulse - 1f) * 0.25f
                        }
                    }
            ) {
                Image(
                    painter = painterResource(id = imageRes),
                    contentDescription = "Human athlete in shorts and t-shirt demonstrating ${exercise.name}",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier.fillMaxSize()
                )

                // High-contrast gradient overlay for readable HUD indicators
                Box(
                    modifier = Modifier
                        .fillMaxSize()
                        .background(
                            Brush.verticalGradient(
                                colors = listOf(
                                    DarkBackground.copy(alpha = 0.6f),
                                    Color.Transparent,
                                    DarkBackground.copy(alpha = 0.85f)
                                )
                            )
                        )
                )
            }

            // Top Status Badges
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .background(DarkBackground.copy(alpha = 0.75f), RoundedCornerShape(20.dp))
                        .border(1.dp, CyanAccent.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(8.dp)
                            .background(if (isPlaying) LimeAccent else TextMuted, CircleShape)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isPlaying) "ACTIVE MOTION GUIDE" else "PAUSED",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                }

                Row(
                    modifier = Modifier
                        .background(DarkBackground.copy(alpha = 0.75f), RoundedCornerShape(20.dp))
                        .border(1.dp, LimeAccent.copy(alpha = 0.4f), RoundedCornerShape(20.dp))
                        .padding(horizontal = 10.dp, vertical = 5.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.North,
                        contentDescription = "Decompression vector",
                        tint = LimeAccent,
                        modifier = Modifier
                            .size(14.dp)
                            .graphicsLayer {
                                translationY = if (isPlaying) -((stretchPulse - 0.98f) * 40f) else 0f
                            }
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "GROWTH +2.4mm",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = LimeAccent
                    )
                }
            }

            // Central Dynamic Motion Vector Graphic
            Column(
                modifier = Modifier
                    .align(Alignment.CenterEnd)
                    .padding(end = 16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Spinal expansion beads
                for (i in 0 until 5) {
                    val beadGlow = (spineGlowProgress + i * 0.2f) % 1f
                    Box(
                        modifier = Modifier
                            .size(10.dp)
                            .graphicsLayer {
                                scaleX = 0.8f + (beadGlow * 0.6f)
                                scaleY = 0.8f + (beadGlow * 0.6f)
                            }
                            .background(
                                color = if (isPlaying) {
                                    if (beadGlow > 0.5f) LimeAccent else CyanAccent
                                } else DarkSurface,
                                shape = CircleShape
                            )
                            .border(1.dp, Color.White.copy(alpha = 0.4f), CircleShape)
                    )
                    if (i < 4) {
                        Box(
                            modifier = Modifier
                                .width(2.dp)
                                .height(if (isPlaying) 10.dp * stretchPulse else 10.dp)
                                .background(CyanAccent.copy(alpha = 0.4f))
                        )
                    }
                }
            }

            // Bottom Real-time Pacing HUD
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomStart)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(Color.Transparent, DarkBackground.copy(alpha = 0.95f))
                        )
                    )
                    .padding(14.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Text(
                            text = "HUMAN FORM: SHORTS & T-SHIRT",
                            style = MaterialTheme.typography.labelSmall,
                            color = CyanAccent,
                            fontWeight = FontWeight.Bold
                        )
                        Text(
                            text = exercise.name,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                    }

                    Box(
                        modifier = Modifier
                            .background(LimeAccent.copy(alpha = 0.15f), RoundedCornerShape(10.dp))
                            .border(1.dp, LimeAccent.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                            .padding(horizontal = 10.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = exercise.category.displayName,
                            style = MaterialTheme.typography.labelSmall,
                            color = LimeAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.height(6.dp))

                // Breathing guidance banner
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceVariant.copy(alpha = 0.7f), RoundedCornerShape(8.dp))
                        .padding(horizontal = 10.dp, vertical = 6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = "Breathing pace",
                        tint = CyanAccent,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = breathPrompt,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                        color = if (breathCycle < 2) CyanAccent else if (breathCycle < 4) LimeAccent else TextPrimary
                    )
                }
            }
        }
    }
}
