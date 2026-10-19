package com.example.ui

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.FitnessCenter
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material.icons.filled.WaterDrop
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.ui.screens.HomeScreen
import com.example.ui.screens.NutritionScreen
import com.example.ui.screens.ProgressScreen
import com.example.ui.screens.RoutineBuilderScreen
import com.example.ui.screens.SleepScreen
import com.example.ui.screens.StreakBadgesScreen
import com.example.ui.screens.WorkoutPlayerScreen
import com.example.ui.screens.WorkoutsScreen
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextSecondary

enum class NavDestination(val label: String, val icon: ImageVector) {
    HOME("Home", Icons.Default.Home),
    WORKOUTS("Workouts", Icons.Default.FitnessCenter),
    PROGRESS("Progress", Icons.Default.TrendingUp),
    NUTRITION("Nutrition", Icons.Default.WaterDrop),
    SLEEP("Sleep & HGH", Icons.Default.Bedtime),
    BADGES("Streaks", Icons.Default.EmojiEvents)
}

@Composable
fun MainScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    var currentScreen by remember { mutableStateOf(NavDestination.HOME) }
    var isBuildingCustomRoutine by remember { mutableStateOf(false) }

    val playerState by viewModel.playerState.collectAsStateWithLifecycle()
    val isWorkoutActive = playerState.activeRoutine != null

    Box(modifier = modifier.fillMaxSize()) {
        Scaffold(
            containerColor = DarkBackground,
            bottomBar = {
                if (!isWorkoutActive && !isBuildingCustomRoutine) {
                    NavigationBar(
                        containerColor = DarkSurface,
                        tonalElevation = 8.dp,
                        modifier = Modifier
                            .windowInsetsPadding(WindowInsets.navigationBars)
                    ) {
                        listOf(
                            NavDestination.HOME,
                            NavDestination.WORKOUTS,
                            NavDestination.PROGRESS,
                            NavDestination.NUTRITION,
                            NavDestination.SLEEP,
                            NavDestination.BADGES
                        ).forEach { dest ->
                            val selected = currentScreen == dest
                            NavigationBarItem(
                                selected = selected,
                                onClick = { currentScreen = dest },
                                icon = {
                                    Icon(
                                        imageVector = dest.icon,
                                        contentDescription = dest.label,
                                        tint = if (selected) NeonCyan else TextMuted
                                    )
                                },
                                label = {
                                    Text(
                                        text = dest.label,
                                        fontSize = 10.sp,
                                        fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium,
                                        color = if (selected) NeonCyan else TextMuted
                                    )
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    selectedIconColor = NeonCyan,
                                    selectedTextColor = NeonCyan,
                                    unselectedIconColor = TextMuted,
                                    unselectedTextColor = TextMuted,
                                    indicatorColor = NeonCyan.copy(alpha = 0.15f)
                                ),
                                modifier = Modifier.testTag("nav_${dest.name.lowercase()}")
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                when (currentScreen) {
                    NavDestination.HOME -> HomeScreen(
                        viewModel = viewModel,
                        onNavigateToWorkouts = { currentScreen = NavDestination.WORKOUTS },
                        onNavigateToProgress = { currentScreen = NavDestination.PROGRESS },
                        onNavigateToNutrition = { currentScreen = NavDestination.NUTRITION },
                        onNavigateToSleep = { currentScreen = NavDestination.SLEEP },
                        onStartWorkout = { routine -> viewModel.startRoutine(routine) }
                    )

                    NavDestination.WORKOUTS -> WorkoutsScreen(
                        viewModel = viewModel,
                        onStartWorkout = { routine -> viewModel.startRoutine(routine) },
                        onOpenRoutineBuilder = { isBuildingCustomRoutine = true }
                    )

                    NavDestination.PROGRESS -> ProgressScreen(
                        viewModel = viewModel
                    )

                    NavDestination.NUTRITION -> NutritionScreen(
                        viewModel = viewModel
                    )

                    NavDestination.SLEEP -> SleepScreen(
                        viewModel = viewModel
                    )

                    NavDestination.BADGES -> StreakBadgesScreen(
                        viewModel = viewModel
                    )
                }
            }
        }

        // Custom Routine Builder Fullscreen Modal
        AnimatedVisibility(
            visible = isBuildingCustomRoutine,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            RoutineBuilderScreen(
                viewModel = viewModel,
                onClose = { isBuildingCustomRoutine = false }
            )
        }

        // Active Interactive Workout Player Fullscreen Modal
        AnimatedVisibility(
            visible = isWorkoutActive,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            WorkoutPlayerScreen(
                viewModel = viewModel,
                onClose = { viewModel.closeWorkoutPlayer() }
            )
        }
    }
}
