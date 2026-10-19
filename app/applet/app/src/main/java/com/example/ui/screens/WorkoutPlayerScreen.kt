package com.example.ui.screens

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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Pause
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.SkipNext
import androidx.compose.material.icons.filled.SkipPrevious
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilledIconButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.IconButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ExerciseRepository
import com.example.ui.components.CircularWorkoutTimer
import com.example.ui.components.HumanExerciseMotionCard
import com.example.ui.theme.BluePrimary
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import kotlinx.coroutines.delay

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun WorkoutPlayerScreen(
    initialExerciseId: String = "cobra_stretch",
    onNavigateBack: () -> Unit = {}
) {
    val exercises = remember { ExerciseRepository.sampleExercises }
    var currentIndex by remember {
        val found = exercises.indexOfFirst { it.id == initialExerciseId }
        mutableIntStateOf(if (found >= 0) found else 0)
    }

    val currentExercise = exercises[currentIndex]
    var remainingSeconds by remember(currentIndex) { mutableIntStateOf(currentExercise.durationSeconds) }
    var isPlaying by remember { mutableStateOf(true) }
    var playerViewMode by remember { mutableStateOf("ANIMATION") } // "ANIMATION" or "TIMER"

    // Countdown timer tick
    LaunchedEffect(isPlaying, remainingSeconds, currentIndex) {
        if (isPlaying && remainingSeconds > 0) {
            delay(1000L)
            remainingSeconds -= 1
        } else if (remainingSeconds == 0 && isPlaying) {
            // Auto advance
            if (currentIndex < exercises.size - 1) {
                currentIndex += 1
            } else {
                isPlaying = false
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Column {
                        Text(
                            text = "Daily Growth Session",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = TextPrimary
                        )
                        Text(
                            text = "Exercise ${currentIndex + 1} of ${exercises.size}",
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                },
                navigationIcon = {
                    IconButton(
                        onClick = onNavigateBack,
                        modifier = Modifier.testTag("player_back_button")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = TextPrimary
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(containerColor = DarkBackground)
            )
        },
        containerColor = DarkBackground
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .verticalScroll(rememberScrollState())
                .padding(horizontal = 16.dp, vertical = 10.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Visual Mode Selector: Human Animation vs Timer Focus
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(DarkSurface, RoundedCornerShape(12.dp))
                    .padding(4.dp),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (playerViewMode == "ANIMATION") CyanAccent else Color.Transparent)
                        .clickable { playerViewMode = "ANIMATION" }
                        .padding(vertical = 8.dp)
                        .testTag("mode_animation_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "HUMAN ANIMATION",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (playerViewMode == "ANIMATION") DarkBackground else TextPrimary
                        )
                    )
                }

                Box(
                    modifier = Modifier
                        .weight(1f)
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (playerViewMode == "TIMER") CyanAccent else Color.Transparent)
                        .clickable { playerViewMode = "TIMER" }
                        .padding(vertical = 8.dp)
                        .testTag("mode_timer_button"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "TIMER ONLY",
                        style = MaterialTheme.typography.labelSmall.copy(
                            fontWeight = FontWeight.Bold,
                            color = if (playerViewMode == "TIMER") DarkBackground else TextPrimary
                        )
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Human Demonstration Card (Half the Screen size)
            if (playerViewMode == "ANIMATION") {
                HumanExerciseMotionCard(
                    exercise = currentExercise,
                    isPlaying = isPlaying,
                    remainingSeconds = remainingSeconds
                )
            } else {
                // Timer Focus View
                CircularWorkoutTimer(
                    totalSeconds = currentExercise.durationSeconds,
                    remainingSeconds = remainingSeconds,
                    isPlaying = isPlaying,
                    size = 200.dp,
                    strokeWidth = 14.dp
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Playback Controls Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (currentIndex > 0) currentIndex -= 1
                    },
                    enabled = currentIndex > 0,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("previous_exercise_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipPrevious,
                        contentDescription = "Previous Exercise",
                        tint = if (currentIndex > 0) TextPrimary else TextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                FilledIconButton(
                    onClick = { isPlaying = !isPlaying },
                    modifier = Modifier
                        .size(68.dp)
                        .testTag("play_pause_button"),
                    colors = IconButtonDefaults.filledIconButtonColors(
                        containerColor = CyanAccent,
                        contentColor = DarkBackground
                    )
                ) {
                    Icon(
                        imageVector = if (isPlaying) Icons.Default.Pause else Icons.Default.PlayArrow,
                        contentDescription = if (isPlaying) "Pause" else "Play",
                        modifier = Modifier.size(36.dp)
                    )
                }

                Spacer(modifier = Modifier.width(20.dp))

                IconButton(
                    onClick = {
                        if (currentIndex < exercises.size - 1) currentIndex += 1
                    },
                    enabled = currentIndex < exercises.size - 1,
                    modifier = Modifier
                        .size(48.dp)
                        .testTag("next_exercise_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.SkipNext,
                        contentDescription = "Next Exercise",
                        tint = if (currentIndex < exercises.size - 1) TextPrimary else TextMuted,
                        modifier = Modifier.size(32.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                IconButton(
                    onClick = { remainingSeconds = currentExercise.durationSeconds },
                    modifier = Modifier
                        .size(40.dp)
                        .testTag("reset_timer_button")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = "Reset Timer",
                        tint = TextSecondary,
                        modifier = Modifier.size(22.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Growth Benefit Card
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = LimeAccent,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Spine Decompression Impact",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = LimeAccent
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = currentExercise.spineBenefit,
                        style = MaterialTheme.typography.bodyMedium,
                        color = TextPrimary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Step-by-Step Instructions
            Card(
                modifier = Modifier.fillMaxWidth(),
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = DarkSurface)
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Form Guidance",
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = CyanAccent
                    )
                    Spacer(modifier = Modifier.height(10.dp))
                    currentExercise.instructions.forEachIndexed { idx, step ->
                        Row(modifier = Modifier.padding(vertical = 4.dp)) {
                            Box(
                                modifier = Modifier
                                    .size(22.dp)
                                    .background(CyanAccent.copy(alpha = 0.2f), CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = "${idx + 1}",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = CyanAccent
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = step,
                                style = MaterialTheme.typography.bodyMedium,
                                color = TextSecondary,
                                modifier = Modifier.weight(1f)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
