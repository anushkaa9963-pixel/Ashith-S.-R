package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bedtime
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MonitorWeight
import androidx.compose.material.icons.filled.Shield
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun BmiPostureCalculator(
    heightCm: Float,
    weightKg: Float,
    isMetric: Boolean,
    modifier: Modifier = Modifier
) {
    val heightMeters = (heightCm / 100f).coerceAtLeast(0.5f)
    val bmi = weightKg / (heightMeters * heightMeters)

    val (category, categoryColor, advice) = when {
        bmi < 18.5f -> Triple(
            "Underweight",
            NeonAmber,
            "Low bone mineral density risk. Increase nutrient & calcium intake to fuel skeletal density."
        )
        bmi < 25f -> Triple(
            "Optimal Alignment",
            NeonLime,
            "Ideal kinetic load on spinal column. Intervertebral discs experience balanced decompression."
        )
        bmi < 30f -> Triple(
            "Overweight",
            NeonAmber,
            "Increased axial load on lumbar discs (L4-S1). Daily decompression is essential to prevent height compression."
        )
        else -> Triple(
            "High Spinal Load",
            Color(0xFFFF5252),
            "Significant compressive force on lumbar curve. Prioritize hanging decompression and core support."
        )
    }

    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface, RoundedCornerShape(16.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Box(
                    modifier = Modifier
                        .size(36.dp)
                        .background(NeonCyan.copy(alpha = 0.15f), CircleShape),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.MonitorWeight,
                        contentDescription = null,
                        tint = NeonCyan,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Spacer(modifier = Modifier.width(10.dp))
                Column {
                    Text(
                        text = "INTEGRATED BMI & POSTURE IMPACT",
                        color = TextMuted,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        letterSpacing = 0.8.sp
                    )
                    Text(
                        text = "Body Mass vs Spinal Load",
                        color = TextPrimary,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }

            Box(
                modifier = Modifier
                    .background(categoryColor.copy(alpha = 0.15f), RoundedCornerShape(20.dp))
                    .border(1.dp, categoryColor.copy(alpha = 0.5f), RoundedCornerShape(20.dp))
                .padding(horizontal = 10.dp, vertical = 4.dp)
            ) {
                Text(
                    text = category,
                    color = categoryColor,
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.Bottom
        ) {
            Column {
                Text(
                    text = "BMI Index",
                    color = TextMuted,
                    fontSize = 12.sp
                )
                Text(
                    text = String.format(Locale.getDefault(), "%.1f", bmi),
                    color = TextPrimary,
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Black
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = if (isMetric) "Weight: ${String.format(Locale.getDefault(), "%.1f kg", weightKg)}"
                    else "Weight: ${String.format(Locale.getDefault(), "%.1f lbs", weightKg * 2.20462f)}",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
                Text(
                    text = if (isMetric) "Height: ${String.format(Locale.getDefault(), "%.1f cm", heightCm)}"
                    else "Height: ${String.format(Locale.getDefault(), "%.1f in", heightCm / 2.54f)}",
                    color = TextSecondary,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Progress bar (range 15 to 35)
        val progress = ((bmi - 15f) / 20f).coerceIn(0.05f, 1f)
        LinearProgressIndicator(
            progress = { progress },
            modifier = Modifier
                .fillMaxWidth()
                .height(8.dp),
            color = categoryColor,
            trackColor = DarkSurfaceVariant,
            strokeCap = StrokeCap.Round
        )

        Spacer(modifier = Modifier.height(12.dp))

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .background(DarkSurfaceVariant.copy(alpha = 0.6f), RoundedCornerShape(10.dp))
                .padding(10.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Info,
                contentDescription = null,
                tint = NeonCyan,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = advice,
                color = TextSecondary,
                fontSize = 12.sp,
                lineHeight = 16.sp
            )
        }
    }
}

@Composable
fun HghOptimalWindowCard(
    modifier: Modifier = Modifier
) {
    Column(
        modifier = modifier
            .fillMaxWidth()
            .background(DarkSurface, RoundedCornerShape(16.dp))
            .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp))
            .padding(16.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
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
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = "HGH CHRONOBIOLOGY",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.8.sp
                )
                Text(
                    text = "10 PM – 2 AM Growth Window",
                    color = TextPrimary,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Visual 24h Bar highlighting 10PM - 2AM
        Column {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(text = "6 PM", color = TextMuted, fontSize = 11.sp)
                Text(text = "10 PM (HGH Peak)", color = NeonPurple, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "2 AM", color = NeonCyan, fontSize = 11.sp, fontWeight = FontWeight.Bold)
                Text(text = "8 AM", color = TextMuted, fontSize = 11.sp)
            }
            Spacer(modifier = Modifier.height(6.dp))
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(14.dp)
                    .background(DarkSurfaceVariant, RoundedCornerShape(7.dp))
            ) {
                // Pre-sleep (6PM - 10PM): 4 hours out of 14h window = 28%
                Box(
                    modifier = Modifier
                        .weight(0.28f)
                        .height(14.dp)
                        .background(DarkSurfaceVariant, RoundedCornerShape(topStart = 7.dp, bottomStart = 7.dp))
                )
                // Optimal HGH Window (10PM - 2AM): 4 hours = 28%
                Box(
                    modifier = Modifier
                        .weight(0.28f)
                        .height(14.dp)
                        .background(
                            androidx.compose.ui.graphics.Brush.horizontalGradient(
                                listOf(NeonPurple, NeonCyan)
                            ),
                            RoundedCornerShape(3.dp)
                        )
                )
                // REM & Morning (2AM - 8AM): 6 hours = 44%
                Box(
                    modifier = Modifier
                        .weight(0.44f)
                        .height(14.dp)
                        .background(DarkSurfaceVariant, RoundedCornerShape(topEnd = 7.dp, bottomEnd = 7.dp))
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        Text(
            text = "Up to 75% of daily Human Growth Hormone (somatotropin) is secreted during slow-wave Stage 3 & 4 non-REM sleep between 10:00 PM and 2:00 AM. Sleep before 10:30 PM maximizes epiphyseal cartilage recovery.",
            color = TextSecondary,
            fontSize = 12.sp,
            lineHeight = 17.sp
        )
    }
}
