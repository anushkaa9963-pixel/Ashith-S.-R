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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Nightlight
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.WbSunny
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.data.model.SleepLog
import com.example.ui.MainViewModel
import com.example.ui.components.HghOptimalWindowCard
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

@Composable
fun SleepScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val todaySleep by viewModel.todaySleep.collectAsStateWithLifecycle()
    val recentSleepLogs by viewModel.recentSleepLogs.collectAsStateWithLifecycle()

    var showLogDialog by remember { mutableStateOf(false) }

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(DarkBackground)
    ) {
        // Top Header
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 20.dp, vertical = 16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = "SOMATOTROPIN RECOVERY",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Deep Sleep & HGH",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Button(
                onClick = { showLogDialog = true },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple),
                shape = RoundedCornerShape(10.dp),
                contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                modifier = Modifier
                    .height(36.dp)
                    .testTag("log_sleep_btn")
            ) {
                Icon(imageVector = Icons.Default.Add, contentDescription = null, modifier = Modifier.size(16.dp))
                Spacer(modifier = Modifier.width(4.dp))
                Text(text = "Log Sleep", fontSize = 12.sp, fontWeight = FontWeight.Bold)
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // HGH Optimal Window Visualizer Card
            item {
                HghOptimalWindowCard()
            }

            // Today's Sleep Status Card
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "LAST NIGHT’S RECOVERY",
                                color = TextMuted,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 1.sp
                            )
                            if (todaySleep?.sleptDuringHghWindow == true) {
                                Box(
                                    modifier = Modifier
                                        .background(NeonPurple.copy(alpha = 0.2f), RoundedCornerShape(12.dp))
                                        .padding(horizontal = 8.dp, vertical = 4.dp)
                                ) {
                                    Text(
                                        text = "✓ HGH Window Captured",
                                        color = NeonPurple,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = todaySleep?.let { "${it.hoursSlept} hrs" } ?: "Not logged yet",
                                    color = TextPrimary,
                                    fontSize = 28.sp,
                                    fontWeight = FontWeight.Black
                                )
                                Text(
                                    text = todaySleep?.let { "Bed: ${it.bedtime} • Wake: ${it.wakeTime}" }
                                        ?: "Tap 'Log Sleep' to track restorative hours",
                                    color = TextSecondary,
                                    fontSize = 12.sp
                                )
                            }

                            todaySleep?.let { sleep ->
                                Row {
                                    for (i in 1..5) {
                                        Icon(
                                            imageVector = Icons.Default.Star,
                                            contentDescription = null,
                                            tint = if (i <= sleep.qualityRating) NeonAmber else DarkSurfaceVariant,
                                            modifier = Modifier.size(18.dp)
                                        )
                                    }
                                }
                            }
                        }

                        if (todaySleep?.notes?.isNotBlank() == true) {
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Note: ${todaySleep?.notes}",
                                color = NeonCyan,
                                fontSize = 12.sp
                            )
                        }
                    }
                }
            }

            // Sleep Hygiene & Spine Decompression Protocol
            item {
                Card(
                    colors = CardDefaults.cardColors(containerColor = DarkSurface),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "NOCTURNAL DISC REHYDRATION PROTOCOL",
                            color = NeonLime,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 1.sp
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "1. Supine Sleeping (Back): Sleeping flat on your back with a small pillow under your knees maintains a neutral lordotic curve.\n" +
                                    "2. Cervical Alignment: Use a low contour pillow so your cervical spine does not crane forward.\n" +
                                    "3. 60-Minute Digital Sunset: Avoid blue light after 9:30 PM to trigger endogenous melatonin and uninterrupted Stage 4 slow-wave sleep.",
                            color = TextSecondary,
                            fontSize = 12.sp,
                            lineHeight = 18.sp
                        )
                    }
                }
            }

            // Recent Sleep Logs
            item {
                Text(
                    text = "RECENT SLEEP HISTORY",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            items(recentSleepLogs) { log ->
                SleepHistoryCard(log = log)
            }
        }
    }

    if (showLogDialog) {
        LogSleepDialog(
            onDismiss = { showLogDialog = false },
            onSave = { hours, bed, wake, quality, inHgh, notes ->
                viewModel.logSleep(hours, bed, wake, quality, inHgh, notes)
                showLogDialog = false
            }
        )
    }
}

@Composable
fun SleepHistoryCard(log: SleepLog) {
    Card(
        colors = CardDefaults.cardColors(containerColor = DarkSurface),
        shape = RoundedCornerShape(12.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, DarkCardBorder, RoundedCornerShape(12.dp))
    ) {
        Row(
            modifier = Modifier
                .padding(14.dp)
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .background(NeonPurple.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Bedtime,
                    contentDescription = null,
                    tint = NeonPurple,
                    modifier = Modifier.size(20.dp)
                )
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(
                        text = "${log.hoursSlept} hrs sleep",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = log.dateKey,
                        color = TextMuted,
                        fontSize = 11.sp
                    )
                }

                Text(
                    text = "${log.bedtime} → ${log.wakeTime} • Deep: ${log.deepSleepHours} hrs",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                if (log.sleptDuringHghWindow) {
                    Text(
                        text = "★ 10 PM - 2 AM HGH Peak Captured",
                        color = NeonPurple,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

@Composable
fun LogSleepDialog(
    onDismiss: () -> Unit,
    onSave: (Float, String, String, Int, Boolean, String) -> Unit
) {
    var hoursInput by remember { mutableStateOf("8.0") }
    var bedtime by remember { mutableStateOf("22:30") }
    var wakeTime by remember { mutableStateOf("06:30") }
    var rating by remember { mutableIntStateOf(5) }
    var inHghWindow by remember { mutableStateOf(true) }
    var notes by remember { mutableStateOf("Felt refreshed, decompressed before bed") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Nightly Rest & HGH",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                OutlinedTextField(
                    value = hoursInput,
                    onValueChange = { hoursInput = it },
                    label = { Text("Total Hours Slept") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("sleep_hours_input")
                )

                Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                    OutlinedTextField(
                        value = bedtime,
                        onValueChange = { bedtime = it },
                        label = { Text("Bedtime") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                    OutlinedTextField(
                        value = wakeTime,
                        onValueChange = { wakeTime = it },
                        label = { Text("Wake Time") },
                        modifier = Modifier.weight(1f),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = NeonPurple,
                            unfocusedBorderColor = DarkCardBorder,
                            focusedTextColor = TextPrimary,
                            unfocusedTextColor = TextPrimary
                        )
                    )
                }

                // HGH Window Checkbox
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { inHghWindow = !inHghWindow }
                ) {
                    Checkbox(
                        checked = inHghWindow,
                        onCheckedChange = { inHghWindow = it },
                        colors = CheckboxDefaults.colors(checkedColor = NeonPurple)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Asleep during 10 PM – 2 AM HGH burst",
                        color = TextSecondary,
                        fontSize = 12.sp
                    )
                }

                // Star Rating
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(text = "Quality: ", color = TextSecondary, fontSize = 12.sp)
                    for (i in 1..5) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = if (i <= rating) NeonAmber else DarkSurfaceVariant,
                            modifier = Modifier
                                .size(28.dp)
                                .clickable { rating = i }
                        )
                    }
                }

                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    label = { Text("Notes") },
                    modifier = Modifier.fillMaxWidth(),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonPurple,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    )
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val hrs = hoursInput.toFloatOrNull() ?: 8f
                    onSave(hrs, bedtime, wakeTime, rating, inHghWindow, notes)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonPurple)
            ) {
                Text("Save Log", color = Color.White, fontWeight = FontWeight.Bold)
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Cancel", color = TextSecondary)
            }
        },
        containerColor = DarkSurface
    )
}
