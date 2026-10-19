package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.shrinkVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Timer
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Exercise
import com.example.data.model.ExerciseCategory
import com.example.data.model.ExerciseRepository
import com.example.ui.components.HumanExerciseMotionCard
import com.example.ui.components.getExerciseHumanImage
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary

@Composable
fun WorkoutsScreen(
    onSelectExercise: (String) -> Unit
) {
    var selectedCategory by remember { mutableStateOf<ExerciseCategory?>(null) }
    val allExercises = remember { ExerciseRepository.sampleExercises }

    val filteredExercises = remember(selectedCategory) {
        if (selectedCategory == null) allExercises else allExercises.filter { it.category == selectedCategory }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(DarkBackground)
            .padding(top = 16.dp)
    ) {
        // Header
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Text(
                text = "Height Growth Library",
                style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                color = TextPrimary
            )
            Text(
                text = "Biomechanical spine stretches and osteogenic workouts demonstrated by athletic human models.",
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Category Filter Chips
        LazyRow(
            contentPadding = PaddingValues(horizontal = 16.dp),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            item {
                CategoryChip(
                    title = "All Moves",
                    isSelected = selectedCategory == null,
                    onClick = { selectedCategory = null },
                    tag = "chip_all"
                )
            }
            items(ExerciseCategory.values()) { cat ->
                CategoryChip(
                    title = cat.displayName,
                    isSelected = selectedCategory == cat,
                    onClick = { selectedCategory = cat },
                    tag = "chip_${cat.name.lowercase()}"
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Exercise List
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
            verticalArrangement = Arrangement.spacedBy(14.dp)
        ) {
            items(filteredExercises) { exercise ->
                ExerciseCard(
                    exercise = exercise,
                    onStartWorkout = { onSelectExercise(exercise.id) }
                )
            }
        }
    }
}

@Composable
fun CategoryChip(
    title: String,
    isSelected: Boolean,
    onClick: () -> Unit,
    tag: String
) {
    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(20.dp))
            .background(if (isSelected) CyanAccent else DarkSurface)
            .border(
                width = 1.dp,
                color = if (isSelected) CyanAccent else DarkSurfaceVariant,
                shape = RoundedCornerShape(20.dp)
            )
            .clickable(onClick = onClick)
            .padding(horizontal = 14.dp, vertical = 8.dp)
            .testTag(tag),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = title,
            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
            color = if (isSelected) DarkBackground else TextPrimary
        )
    }
}

@Composable
fun ExerciseCard(
    exercise: Exercise,
    onStartWorkout: () -> Unit
) {
    var showAnimationPreview by remember { mutableStateOf(false) }
    val imageRes = getExerciseHumanImage(exercise.id, exercise.category)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .testTag("exercise_card_${exercise.id}"),
        shape = RoundedCornerShape(18.dp),
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                // Human Photo Thumbnail (Wearing shorts & t-shirt)
                Box(
                    modifier = Modifier
                        .size(72.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .border(1.dp, CyanAccent.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                ) {
                    Image(
                        painter = painterResource(id = imageRes),
                        contentDescription = exercise.name,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )

                    // Small tag
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomCenter)
                            .fillMaxWidth()
                            .background(DarkBackground.copy(alpha = 0.8f))
                            .padding(vertical = 2.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "HUMAN",
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 9.sp),
                            color = LimeAccent,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }

                Spacer(modifier = Modifier.width(12.dp))

                // Exercise Info
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = exercise.category.displayName,
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = exercise.name,
                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                        color = TextPrimary
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Timer,
                            contentDescription = null,
                            tint = TextMuted,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = exercise.targetReps,
                            style = MaterialTheme.typography.bodyMedium,
                            color = TextSecondary
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = exercise.description,
                style = MaterialTheme.typography.bodyMedium,
                color = TextSecondary
            )

            Spacer(modifier = Modifier.height(12.dp))

            // Action Buttons: Watch Animation toggle and Start Exercise
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    modifier = Modifier
                        .clip(RoundedCornerShape(8.dp))
                        .background(if (showAnimationPreview) CyanAccent.copy(alpha = 0.2f) else DarkSurfaceVariant)
                        .clickable { showAnimationPreview = !showAnimationPreview }
                        .padding(horizontal = 12.dp, vertical = 8.dp)
                        .testTag("preview_toggle_${exercise.id}"),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = if (showAnimationPreview) Icons.Default.Close else Icons.Default.PlayArrow,
                        contentDescription = "Animation preview",
                        tint = if (showAnimationPreview) CyanAccent else TextPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (showAnimationPreview) "Hide Guide" else "Watch Animation",
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                        color = if (showAnimationPreview) CyanAccent else TextPrimary
                    )
                }

                Button(
                    onClick = onStartWorkout,
                    colors = ButtonDefaults.buttonColors(
                        containerColor = LimeAccent,
                        contentColor = DarkBackground
                    ),
                    shape = RoundedCornerShape(10.dp),
                    modifier = Modifier.testTag("start_exercise_${exercise.id}")
                ) {
                    Text(
                        text = "Start Move",
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // Expandable Half-Screen Animation Card
            AnimatedVisibility(
                visible = showAnimationPreview,
                enter = fadeIn() + expandVertically(),
                exit = fadeOut() + shrinkVertically()
            ) {
                Column(modifier = Modifier.padding(top = 12.dp)) {
                    Text(
                        text = "Interactive Human Motion Demonstration",
                        style = MaterialTheme.typography.labelSmall,
                        color = CyanAccent,
                        fontWeight = FontWeight.Bold
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    HumanExerciseMotionCard(
                        exercise = exercise,
                        isPlaying = true,
                        remainingSeconds = 30
                    )
                }
            }
        }
    }
}
