package com.example.ui.screens

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
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.NutritionLog
import com.example.data.model.PredefinedRoutines
import com.example.data.model.WorkoutRoutine
import com.example.ui.MainViewModel
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonAmber
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.NeonPurple
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.util.Locale

@Composable
fun HomeScreen(
    viewModel: MainViewModel,
    onNavigateToWorkouts: () -> Unit,
    onNavigateToProgress: () -> Unit,
    onNavigateToNutrition: () -> Unit,
    onNavigateToSleep: () -> Unit,
    onStartWorkout: (WorkoutRoutine) -> Unit,
    modifier: Modifier = Modifier
) {
    val quote by viewModel.quoteOfTheDay.collectAsStateWithLifecycle()
    val streak by viewModel.currentStreak.collectAsStateWithLifecycle()
    val latestHeight by viewModel.latestHeight.collectAsStateWithLifecycle()
    val todayNutrition by viewModel.todayNutrition.collectAsStateWithLifecycle()
    val todaySleep by viewModel.todaySleep.collectAsStateWithLifecycle()
    val isMetric by viewModel.isMetric.collectAsStateWithLifecycle()

    LazyColumn(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground),
        contentPadding = PaddingValues(bottom = 96.dp)
    ) {
        // Top Athletic Header
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(
                        Brush.verticalGradient(
                            listOf(Color(0xFF0F1E36), DarkBackground)
                        )
                    )
                    .padding(horizontal = 20.dp, vertical = 20.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = "POSTURE",
                                color = TextPrimary,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            )
                            Text(
                                text = "GROW",
                                color = NeonCyan,
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Black,
                                letterSpacing = (-0.5).sp
                            )
                        }
                        Text(
                            text = "Spine Elongation & Posture Conditioning",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }

                    // Streak Pill
                    Box(
                        modifier = Modifier
                            .background(NeonAmber.copy(alpha = 0.15f), RoundedCornerShape(24.dp))
                            .border(1.dp, NeonAmber.copy(alpha = 0.5f), RoundedCornerShape(24.dp))
                            .padding(horizontal = 12.dp, vertical = 6.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocalFireDepartment,
                                contentDescription = null,
                                tint = NeonAmber,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "$streak DAY STREAK",
                                color = NeonAmber,
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }
                }
            }
        }

        // Daily Motivation Banner
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = DarkSurface),
                shape = RoundedCornerShape(16.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
                    .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "DAILY GROWTH MOTIVATION",
                            color = NeonLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Text(
                            text = quote.category,
                            color = TextMuted,
                            fontSize = 11.sp
                        )
                    }
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "“${quote.quote}”",
                        color = TextPrimary,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Medium,
                        lineHeight = 20.sp
                    )
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "— ${quote.author}",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(20.dp)) }

        // Daily Routine Scheduler: Morning & Bedtime Programs
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "DAILY SCHEDULED PROTOCOLS",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    // Morning Wake-Up Program
                    ScheduledRoutineCard(
                        title = "Morning Stretches",
                        timeTag = "AM WAKE-UP",
                        icon = Icons.Default.WbSunny,
                        iconTint = NeonAmber,
                        duration = "2.5 mins",
                        exerciseCount = "5 moves",
                        accentColor = NeonAmber,
                        modifier = Modifier.weight(1f),
                        onStart = { onStartWorkout(PredefinedRoutines.MORNING_WAKEUP) }
                    )

                    // Before-Bed Decompression Program
                    ScheduledRoutineCard(
                        title = "Bedtime Decompress",
                        timeTag = "PM REHYDRATE",
                        icon = Icons.Default.Bedtime,
                        iconTint = NeonPurple,
                        duration = "2.5 mins",
                        exerciseCount = "5 moves",
                        accentColor = NeonPurple,
                        modifier = Modifier.weight(1f),
                        onStart = { onStartWorkout(PredefinedRoutines.BEDTIME_DECOMPRESSION) }
                    )
                }
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // Core Height Circuits
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "CORE HEIGHT CIRCUITS",
                        color = TextMuted,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 1.sp
                    )
                    Text(
                        text = "See All",
                        color = NeonCyan,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.clickable { onNavigateToWorkouts() }
                    )
                }
                Spacer(modifier = Modifier.height(12.dp))

                // Circuit 1: Spine Decompression
                CoreCircuitCard(
                    routine = PredefinedRoutines.SPINE_DECOMPRESSION,
                    accentColor = NeonCyan,
                    onStart = { onStartWorkout(PredefinedRoutines.SPINE_DECOMPRESSION) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Circuit 2: Posture Correction
                CoreCircuitCard(
                    routine = PredefinedRoutines.POSTURE_ALIGNMENT,
                    accentColor = NeonLime,
                    onStart = { onStartWorkout(PredefinedRoutines.POSTURE_ALIGNMENT) }
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Circuit 3: Lower Body Leg Lengthening
                CoreCircuitCard(
                    routine = PredefinedRoutines.LEG_LENGTHENING,
                    accentColor = NeonAmber,
                    onStart = { onStartWorkout(PredefinedRoutines.LEG_LENGTHENING) }
                )
            }
        }

        item { Spacer(modifier = Modifier.height(24.dp)) }

        // Growth Status Highlights (Height, Nutrition, Sleep)
        item {
            Column(modifier = Modifier.padding(horizontal = 16.dp)) {
                Text(
                    text = "GROWTH METRIC TRACKERS",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Spacer(modifier = Modifier.height(12.dp))

                // Height Milestone summary card
                GrowthTrackerQuickCard(
                    title = "Height Progression",
                    value = latestHeight?.let {
                        if (isMetric) String.format(Locale.getDefault(), "%.1f cm", it.heightCm)
                        else String.format(Locale.getDefault(), "%.1f in", it.heightCm / 2.54f)
                    } ?: "174.5 cm",
                    subtitle = latestHeight?.note ?: "Active decompression logging",
                    icon = Icons.Default.TrendingUp,
                    iconColor = NeonCyan,
                    onClick = onNavigateToProgress
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Nutrition Daily summary card
                val waterMl = todayNutrition?.waterMl ?: 0
                val calciumMg = todayNutrition?.calciumMg ?: 0
                GrowthTrackerQuickCard(
                    title = "Nutritional Growth Log",
                    value = "$calciumMg / ${NutritionLog.TARGET_CALCIUM_MG} mg Calcium",
                    subtitle = "Water: $waterMl / ${NutritionLog.TARGET_WATER_ML} ml",
                    icon = Icons.Default.WaterDrop,
                    iconColor = NeonLime,
                    onClick = onNavigateToNutrition
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Sleep & HGH summary card
                GrowthTrackerQuickCard(
                    title = "Deep Sleep & HGH Window",
                    value = todaySleep?.let { "${it.hoursSlept} hrs slept" } ?: "8.2 hrs logged",
                    subtitle = "Prime 10 PM - 2 AM Somatotropin release window",
                    icon = Icons.Default.Bedtime,
                    iconColor = NeonPurple,
                    onClick = onNavigateToSleep
                )
            }
        }
    }
}

@Composable
fun ScheduledRoutineCard(
    title: String,
    timeTag: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconTint: Color,
    duration: String,
    exerciseCount: String,
    accentColor: Color,
    modifier: Modifier = Modifier,
    onStart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = modifier
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .clickable { onStart() }
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .size(32.dp)
                        .background(iconTint.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = icon,
                        contentDescription = null,
                        tint = iconTint,
                        modifier = Modifier.size(18.dp)
                    )
                }
                Box(
                    modifier = Modifier
                        .background(accentColor.copy(alpha = 0.15f), RoundedCornerShape(12.dp))
                        .padding(horizontal = 6.dp, vertical = 2.dp)
                ) {
                    Text(
                        text = timeTag,
                        color = accentColor,
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Black
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = title,
                color = TextPrimary,
                fontSize = 14.sp,
                fontWeight = FontWeight.Bold
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "$exerciseCount • $duration",
                color = TextSecondary,
                fontSize = 11.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            Button(
                onClick = onStart,
                colors = ButtonDefaults.buttonColors(containerColor = accentColor.copy(alpha = 0.15f)),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 4.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(34.dp)
                    .border(1.dp, accentColor.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = null,
                        tint = accentColor,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Start",
                        color = accentColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}

@Composable
fun CoreCircuitCard(
    routine: WorkoutRoutine,
    accentColor: Color,
    onStart: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .clickable { onStart() }
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = routine.title,
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = routine.subtitle,
                        color = accentColor,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .background(accentColor, CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.PlayArrow,
                        contentDescription = "Start Workout",
                        tint = Color(0xFF041E26),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = routine.description,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 17.sp
            )

            Spacer(modifier = Modifier.height(12.dp))
            // Exercise tags
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                routine.exercises.forEach { exercise ->
                    Box(
                        modifier = Modifier
                            .background(DarkSurfaceVariant, RoundedCornerShape(6.dp))
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    ) {
                        Text(
                            text = exercise.name.split(" ").first(),
                            color = TextSecondary,
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun GrowthTrackerQuickCard(
    title: String,
    value: String,
    subtitle: String,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    iconColor: Color,
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(14.dp))
            .clickable { onClick() }
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(iconColor.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = icon,
                    contentDescription = null,
                    tint = iconColor,
                    modifier = Modifier.size(20.dp)
                )
            }
            Spacer(modifier = Modifier.width(12.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = title,
                    color = TextPrimary,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Bold
                )
                Text(
                    text = value,
                    color = iconColor,
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
                Text(
                    text = subtitle,
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
            Icon(
                imageVector = Icons.Default.ChevronRight,
                contentDescription = null,
                tint = TextMuted,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}
