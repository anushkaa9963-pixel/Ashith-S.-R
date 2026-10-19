package com.example.ui.screens

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import com.example.ui.components.HumanExerciseMotionCard
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FastForward
import androidx.compose.material.icons.filled.FastRewind
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Replay
import androidx.compose.material.icons.filled.SelfImprovement
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.MainViewModel
import com.example.ui.components.CircularWorkoutTimer
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WorkoutPlayerScreen(
    viewModel: MainViewModel,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val activeRoutine = playerState.activeRoutine ?: run {
        onClose()
        return
    }

    var showExitDialog by remember { mutableStateOf(false) }

    BackHandler {
        if (!playerState.isCompleted) {
            showExitDialog = true
        } else {
            viewModel.closeWorkoutPlayer()
            onClose()
        }
    }

    if (showExitDialog) {
        AlertDialog(
            onDismissRequest = { showExitDialog = false },
            title = { Text("Quit Workout?", color = TextPrimary) },
            text = {
                Text(
                    "You're in the middle of spinal decompression. Exiting now will cancel progress for this session.",
                    color = TextSecondary
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showExitDialog = false
                        viewModel.closeWorkoutPlayer()
                        onClose()
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFEF4444))
                ) {
                    Text("Quit")
                }
            },
            dismissButton = {
                TextButton(onClick = { showExitDialog = false }) {
                    Text("Keep Going", color = NeonCyan)
                }
            },
            containerColor = DarkSurface
        )
    }

    // Finished Celebration Screen
    if (playerState.isCompleted) {
        WorkoutCompletedView(
            routineTitle = activeRoutine.title,
            totalTimeSeconds = playerState.totalElapsedTime,
            exerciseCount = activeRoutine.exercises.size,
            onDone = {
                viewModel.closeWorkoutPlayer()
                onClose()
            }
        )
        return
    }

    val currentExercise = playerState.currentExercise ?: return
    val nextExercise = playerState.nextExercise

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(horizontal = 20.dp, vertical = 16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Top Bar: Exit button & Routine title & Progress
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            IconButton(
                onClick = { showExitDialog = true },
                modifier = Modifier.testTag("close_workout_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.Close,
                    contentDescription = "Close Workout",
                    tint = TextPrimary
                )
            }

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    text = activeRoutine.title.uppercase(),
                    color = NeonCyan,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Black,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Exercise ${playerState.currentExerciseIndex + 1} of ${activeRoutine.exercises.size}",
                    color = TextSecondary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Medium
                )
            }

            IconButton(onClick = { viewModel.restartCurrentExercise() }) {
                Icon(
                    imageVector = Icons.Default.Replay,
                    contentDescription = "Restart Exercise",
                    tint = TextSecondary
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Overall Routine Progress Bar
        val routineProgress = (playerState.currentExerciseIndex.toFloat() +
                (1f - playerState.progressPercent)) / activeRoutine.exercises.size.toFloat()
        LinearProgressIndicator(
            progress = { routineProgress.coerceIn(0f, 1f) },
            modifier = Modifier
                .fillMaxWidth()
                .height(6.dp),
            color = NeonCyan,
            trackColor = DarkSurfaceVariant,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(20.dp))

        // Exercise Title & Category Chip
        Box(
            modifier = Modifier
                .background(NeonLime.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                .border(1.dp, NeonLime.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 12.dp, vertical = 6.dp)
        ) {
            Text(
                text = currentExercise.category.displayName.uppercase(),
                color = NeonLime,
                fontSize = 11.sp,
                fontWeight = FontWeight.Black,
                letterSpacing = 0.8.sp
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = currentExercise.name,
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(4.dp))

        Text(
            text = "Target: ${currentExercise.targetArea}",
            color = TextSecondary,
            fontSize = 13.sp,
            fontWeight = FontWeight.Medium
        )

        Spacer(modifier = Modifier.height(14.dp))

        // Visual Mode Toggle: Animated Human Demonstration vs Timer Only
        var playerViewMode by remember { mutableStateOf("ANIMATION") }

        Row(
            modifier = Modifier
                .background(DarkSurfaceVariant, RoundedCornerShape(20.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(20.dp))
                .padding(3.dp),
            horizontalArrangement = Arrangement.Center
        ) {
            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (playerViewMode == "ANIMATION") NeonCyan else Color.Transparent)
                    .clickable { playerViewMode = "ANIMATION" }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "HUMAN ANIMATION",
                    color = if (playerViewMode == "ANIMATION") Color(0xFF041E26) else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }

            Box(
                modifier = Modifier
                    .clip(RoundedCornerShape(16.dp))
                    .background(if (playerViewMode == "TIMER") NeonCyan else Color.Transparent)
                    .clickable { playerViewMode = "TIMER" }
                    .padding(horizontal = 14.dp, vertical = 6.dp),
                contentAlignment = Alignment.Center
            ) {
                Text(
                    text = "TIMER ONLY",
                    color = if (playerViewMode == "TIMER") Color(0xFF041E26) else TextSecondary,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        if (playerViewMode == "ANIMATION") {
            // Half-screen Human Demonstration Animation Card (showing human in shorts & t-shirt)
            HumanExerciseMotionCard(
                exercise = currentExercise,
                isPlaying = playerState.isPlaying,
                remainingSeconds = playerState.remainingSeconds,
                totalSeconds = playerState.totalExerciseSeconds,
                heightDp = 310
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Interactive Circular Countdown Timer
            CircularWorkoutTimer(
                totalSeconds = playerState.totalExerciseSeconds,
                remainingSeconds = playerState.remainingSeconds,
                isPlaying = playerState.isPlaying,
                accentColor = NeonCyan
            )
        } else {
            // Full Timer Focus
            CircularWorkoutTimer(
                totalSeconds = playerState.totalExerciseSeconds,
                remainingSeconds = playerState.remainingSeconds,
                isPlaying = playerState.isPlaying,
                accentColor = NeonCyan
            )
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Fluid Controls: Prev, Play/Pause, Next
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Previous
            IconButton(
                onClick = { viewModel.previousExercise() },
                enabled = playerState.currentExerciseIndex > 0,
                modifier = Modifier
                    .size(52.dp)
                    .background(DarkSurfaceVariant, CircleShape)
                    .testTag("prev_exercise_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.FastRewind,
                    contentDescription = "Previous Exercise",
                    tint = if (playerState.currentExerciseIndex > 0) TextPrimary else TextMuted,
                    modifier = Modifier.size(26.dp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Primary Play / Pause
            Button(
                onClick = { viewModel.togglePlayPause() },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (playerState.isPlaying) NeonAmber else NeonCyan
                ),
                shape = CircleShape,
                modifier = Modifier
                    .size(76.dp)
                    .testTag("play_pause_btn")
            ) {
                Icon(
                    imageVector = if (playerState.isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                    contentDescription = if (playerState.isPlaying) "Pause" else "Play",
                    tint = Color(0xFF041E26),
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.width(24.dp))

            // Skip / Next
            IconButton(
                onClick = { viewModel.skipToNextExercise() },
                modifier = Modifier
                    .size(52.dp)
                    .background(DarkSurfaceVariant, CircleShape)
                    .testTag("skip_exercise_btn")
            ) {
                Icon(
                    imageVector = Icons.Default.FastForward,
                    contentDescription = "Skip Exercise",
                    tint = TextPrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(28.dp))

        // Form Instructions & Biomechanical Spine Benefit Card
        Card(
            colors = CardDefaults.cardColors(containerColor = DarkSurface),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.SelfImprovement,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "FORM INSTRUCTIONS & BREATHING",
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = currentExercise.instructions,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    lineHeight = 19.sp
                )

                Spacer(modifier = Modifier.height(10.dp))
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(DarkSurfaceVariant, RoundedCornerShape(10.dp))
                        .padding(10.dp)
                ) {
                    Text(
                        text = "⚡ Spine Extension Benefit: ${currentExercise.spineBenefit}",
                        color = NeonLime,
                        fontSize = 12.sp,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        // Up next peek banner
        if (nextExercise != null) {
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurfaceVariant.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "NEXT UP:",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = nextExercise.name,
                    color = TextPrimary,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
fun WorkoutCompletedView(
    routineTitle: String,
    totalTimeSeconds: Int,
    exerciseCount: Int,
    onDone: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(24.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Box(
            modifier = Modifier
                .size(96.dp)
                .background(NeonLime.copy(alpha = 0.15f), CircleShape)
                .border(2.dp, NeonLime, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = Icons.Default.EmojiEvents,
                contentDescription = null,
                tint = NeonLime,
                modifier = Modifier.size(48.dp)
            )
        }

        Spacer(modifier = Modifier.height(24.dp))

        Text(
            text = "DECOMPRESSION COMPLETE!",
            color = NeonLime,
            fontSize = 13.sp,
            fontWeight = FontWeight.Black,
            letterSpacing = 1.2.sp
        )

        Spacer(modifier = Modifier.height(6.dp))

        Text(
            text = routineTitle,
            color = TextPrimary,
            fontSize = 24.sp,
            fontWeight = FontWeight.Black,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(10.dp))

        Text(
            text = "Your spinal discs are rehydrated and decompressed. Consistency unlocks lasting vertical posture.",
            color = TextSecondary,
            fontSize = 14.sp,
            textAlign = TextAlign.Center,
            lineHeight = 20.sp
        )

        Spacer(modifier = Modifier.height(28.dp))

        // Summary Stats Row
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "EXERCISES", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "$exerciseCount", color = TextPrimary, fontSize = 22.sp, fontWeight = FontWeight.Black)
                }
            }

            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(14.dp),
                modifier = Modifier
                    .weight(1f)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
                    .padding(14.dp)
            ) {
                Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.fillMaxWidth()) {
                    Text(text = "TIME DURATION", color = TextMuted, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                    Text(text = "${totalTimeSeconds}s", color = NeonCyan, fontSize = 22.sp, fontWeight = FontWeight.Black)
                }
            }
        }

        Spacer(modifier = Modifier.height(36.dp))

        Button(
            onClick = onDone,
            colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(52.dp)
                .testTag("workout_complete_continue_btn")
        ) {
            Text(
                text = "Continue to Dashboard",
                color = Color(0xFF041E26),
                fontSize = 15.sp,
                fontWeight = FontWeight.Bold
            )
        }
    }
}
