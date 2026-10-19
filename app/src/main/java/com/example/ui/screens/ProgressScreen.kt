package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
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
import com.example.ui.MainViewModel
import com.example.ui.components.BmiPostureCalculator
import com.example.ui.components.InteractiveHeightChart
import com.example.ui.theme.DarkBackground
import com.example.ui.theme.DarkCardBorder
import com.example.ui.theme.DarkSurface
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.NeonCyan
import com.example.ui.theme.NeonLime
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary
import com.example.ui.theme.TextSecondary
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProgressScreen(
    viewModel: MainViewModel,
    modifier: Modifier = Modifier
) {
    val heightLogs by viewModel.heightLogs.collectAsStateWithLifecycle()
    val isMetric by viewModel.isMetric.collectAsStateWithLifecycle()
    val targetHeightCm by viewModel.targetHeightCm.collectAsStateWithLifecycle()

    var showAddDialog by remember { mutableStateOf(false) }

    val currentHeightCm = heightLogs.lastOrNull()?.heightCm ?: 174.5f
    val currentWeightKg = heightLogs.lastOrNull()?.weightKg ?: 66f

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
                    text = "PROGRESS & ANALYTICS",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Text(
                    text = "Height Dashboard",
                    color = TextPrimary,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black
                )
            }

            // Unit toggle (Metric / Imperial)
            OutlinedButton(
                onClick = { viewModel.toggleMetric() },
                colors = ButtonDefaults.outlinedButtonColors(contentColor = NeonCyan),
                border = androidx.compose.foundation.BorderStroke(1.dp, NeonCyan.copy(alpha = 0.5f)),
                shape = RoundedCornerShape(20.dp),
                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 4.dp),
                modifier = Modifier.height(34.dp)
            ) {
                Icon(
                    imageVector = Icons.Default.SwapHoriz,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isMetric) "Metric (cm/kg)" else "Imperial (in/lb)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Visual Height Chart
            item {
                InteractiveHeightChart(
                    logs = heightLogs,
                    isMetric = isMetric
                )
            }

            // Quick Action: Log New Height Checkpoint
            item {
                Button(
                    onClick = { showAddDialog = true },
                    colors = ButtonDefaults.buttonColors(containerColor = NeonCyan),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("log_new_height_btn")
                ) {
                    Icon(
                        imageVector = Icons.Default.Add,
                        contentDescription = null,
                        tint = Color(0xFF041E26),
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Record Height Measurement",
                        color = Color(0xFF041E26),
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            // BMI & Posture Biomechanics Calculator
            item {
                BmiPostureCalculator(
                    heightCm = currentHeightCm,
                    weightKg = currentWeightKg,
                    isMetric = isMetric
                )
            }

            // Growth Milestone History Log
            item {
                Text(
                    text = "GROWTH MILESTONE LOG",
                    color = TextMuted,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
            }

            val reversedLogs = heightLogs.reversed()
            items(reversedLogs) { log ->
                MilestoneHistoryCard(log = log, isMetric = isMetric)
            }
        }
    }

    if (showAddDialog) {
        AddHeightDialog(
            isMetric = isMetric,
            initialHeightCm = currentHeightCm,
            initialWeightKg = currentWeightKg,
            onDismiss = { showAddDialog = false },
            onSave = { h, w, note ->
                viewModel.addHeightEntry(h, w, note)
                showAddDialog = false
            }
        )
    }
}

@Composable
fun MilestoneHistoryCard(
    log: com.example.data.model.HeightLog,
    isMetric: Boolean
) {
    val sdf = SimpleDateFormat("MMM d, yyyy • h:mm a", Locale.getDefault())
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
                    .background(NeonLime.copy(alpha = 0.15f), CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.CheckCircle,
                    contentDescription = null,
                    tint = NeonLime,
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
                        text = if (isMetric) String.format(Locale.getDefault(), "%.1f cm", log.heightCm)
                        else String.format(Locale.getDefault(), "%.1f in", log.heightCm / 2.54f),
                        color = TextPrimary,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                    Text(
                        text = if (isMetric) "${log.weightKg} kg" else String.format(Locale.getDefault(), "%.1f lbs", log.weightKg * 2.20462f),
                        color = TextMuted,
                        fontSize = 12.sp
                    )
                }

                if (log.note.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = log.note,
                        color = NeonCyan,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Medium
                    )
                }

                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = sdf.format(Date(log.timestamp)),
                    color = TextMuted,
                    fontSize = 11.sp
                )
            }
        }
    }
}

@Composable
fun AddHeightDialog(
    isMetric: Boolean,
    initialHeightCm: Float,
    initialWeightKg: Float,
    onDismiss: () -> Unit,
    onSave: (Float, Float, String) -> Unit
) {
    var heightInput by remember {
        mutableStateOf(
            if (isMetric) String.format(Locale.getDefault(), "%.1f", initialHeightCm)
            else String.format(Locale.getDefault(), "%.1f", initialHeightCm / 2.54f)
        )
    }
    var weightInput by remember {
        mutableStateOf(
            if (isMetric) String.format(Locale.getDefault(), "%.1f", initialWeightKg)
            else String.format(Locale.getDefault(), "%.1f", initialWeightKg * 2.20462f)
        )
    }
    var noteInput by remember { mutableStateOf("Post-decompression check") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = {
            Text(
                text = "Log Height Measurement",
                color = TextPrimary,
                fontWeight = FontWeight.Bold
            )
        },
        text = {
            Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                Text(
                    text = "Measure barefoot with heels, sacrum, and thoracic spine flush against a vertical wall.",
                    color = TextSecondary,
                    fontSize = 12.sp
                )

                OutlinedTextField(
                    value = heightInput,
                    onValueChange = { heightInput = it },
                    label = { Text(if (isMetric) "Height (cm)" else "Height (inches)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("height_input_field")
                )

                OutlinedTextField(
                    value = weightInput,
                    onValueChange = { weightInput = it },
                    label = { Text(if (isMetric) "Weight (kg)" else "Weight (lbs)") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth().testTag("weight_input_field")
                )

                OutlinedTextField(
                    value = noteInput,
                    onValueChange = { noteInput = it },
                    label = { Text("Measurement Notes") },
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = NeonCyan,
                        unfocusedBorderColor = DarkCardBorder,
                        focusedTextColor = TextPrimary,
                        unfocusedTextColor = TextPrimary
                    ),
                    modifier = Modifier.fillMaxWidth()
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    val rawHeight = heightInput.toFloatOrNull() ?: initialHeightCm
                    val finalHeightCm = if (isMetric) rawHeight else rawHeight * 2.54f

                    val rawWeight = weightInput.toFloatOrNull() ?: initialWeightKg
                    val finalWeightKg = if (isMetric) rawWeight else rawWeight / 2.20462f

                    onSave(finalHeightCm, finalWeightKg, noteInput)
                },
                colors = ButtonDefaults.buttonColors(containerColor = NeonLime)
            ) {
                Text("Save Entry", color = Color(0xFF00220D), fontWeight = FontWeight.Bold)
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
