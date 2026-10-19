package com.example.ui.components

import androidx.compose.foundation.Canvas
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
import androidx.compose.material.icons.filled.TrendingUp
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HeightLog
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
fun InteractiveHeightChart(
    logs: List<HeightLog>,
    isMetric: Boolean,
    modifier: Modifier = Modifier
) {
    if (logs.isEmpty()) {
        Box(
            modifier = modifier
                .fillMaxWidth()
                .height(180.dp)
                .background(DarkSurface, RoundedCornerShape(16.dp))
                .border(1.dp, DarkCardBorder, RoundedCornerShape(16.dp)),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Icon(
                    imageVector = Icons.Default.TrendingUp,
                    contentDescription = null,
                    tint = TextMuted,
                    modifier = Modifier.size(36.dp)
                )
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "No height logs yet. Add your baseline!",
                    color = TextSecondary,
                    fontSize = 14.sp
                )
            }
        }
        return
    }

    val displayLogs = logs.takeLast(10)
    val heights = displayLogs.map { log ->
        if (isMetric) log.heightCm else log.heightCm / 2.54f
    }
    val minHeight = (heights.minOrNull() ?: 170f) - 1.5f
    val maxHeight = (heights.maxOrNull() ?: 175f) + 1.5f
    val range = (maxHeight - minHeight).coerceAtLeast(1f)

    val firstVal = heights.first()
    val lastVal = heights.last()
    val totalGain = lastVal - firstVal

    val unitLabel = if (isMetric) "cm" else "in"

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
            Column {
                Text(
                    text = "HEIGHT PROGRESSION",
                    color = TextMuted,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 1.sp
                )
                Row(verticalAlignment = Alignment.Bottom) {
                    Text(
                        text = String.format(Locale.getDefault(), "%.1f", lastVal),
                        color = TextPrimary,
                        fontSize = 28.sp,
                        fontWeight = FontWeight.Black
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = unitLabel,
                        color = NeonCyan,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(bottom = 4.dp)
                    )
                }
            }

            // Gain badge
            Box(
                modifier = Modifier
                    .background(
                        if (totalGain >= 0) NeonLime.copy(alpha = 0.15f) else Color.Red.copy(alpha = 0.15f),
                        RoundedCornerShape(20.dp)
                    )
                    .border(
                        1.dp,
                        if (totalGain >= 0) NeonLime.copy(alpha = 0.5f) else Color.Red.copy(alpha = 0.5f),
                        RoundedCornerShape(20.dp)
                    )
                    .padding(horizontal = 12.dp, vertical = 6.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.TrendingUp,
                        contentDescription = null,
                        tint = if (totalGain >= 0) NeonLime else Color.Red,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = (if (totalGain >= 0) "+" else "") + String.format(Locale.getDefault(), "%.1f %s", totalGain, unitLabel),
                        color = if (totalGain >= 0) NeonLime else Color.Red,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Custom Canvas Chart
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(140.dp)
        ) {
            Canvas(modifier = Modifier.matchParentSize()) {
                val width = size.width
                val height = size.height
                val paddingX = 16.dp.toPx()
                val paddingY = 16.dp.toPx()

                val usableWidth = width - (paddingX * 2)
                val usableHeight = height - (paddingY * 2)

                // Grid lines (3 horizontal lines)
                for (i in 0..2) {
                    val y = paddingY + (usableHeight * (i / 2f))
                    drawLine(
                        color = DarkSurfaceVariant,
                        start = Offset(paddingX, y),
                        end = Offset(width - paddingX, y),
                        strokeWidth = 1.dp.toPx()
                    )
                }

                if (displayLogs.size < 2) {
                    val x = width / 2f
                    val y = paddingY + usableHeight / 2f
                    drawCircle(color = NeonCyan, radius = 6.dp.toPx(), center = Offset(x, y))
                    return@Canvas
                }

                val points = displayLogs.mapIndexed { index, _ ->
                    val h = heights[index]
                    val x = paddingX + (usableWidth * (index.toFloat() / (displayLogs.size - 1)))
                    val normalizedH = (h - minHeight) / range
                    val y = paddingY + usableHeight * (1f - normalizedH)
                    Offset(x, y)
                }

                // Gradient fill under the curve
                val fillPath = Path().apply {
                    moveTo(points.first().x, height - paddingY)
                    points.forEach { lineTo(it.x, it.y) }
                    lineTo(points.last().x, height - paddingY)
                    close()
                }

                drawPath(
                    path = fillPath,
                    brush = Brush.verticalGradient(
                        colors = listOf(
                            NeonCyan.copy(alpha = 0.35f),
                            NeonCyan.copy(alpha = 0.0f)
                        ),
                        startY = paddingY,
                        endY = height
                    )
                )

                // The glowing line
                val strokePath = Path().apply {
                    moveTo(points.first().x, points.first().y)
                    for (i in 1 until points.size) {
                        lineTo(points[i].x, points[i].y)
                    }
                }

                drawPath(
                    path = strokePath,
                    color = NeonCyan,
                    style = Stroke(width = 3.5.dp.toPx(), cap = StrokeCap.Round)
                )

                // Plot dots
                points.forEachIndexed { idx, pt ->
                    val isLast = idx == points.size - 1
                    drawCircle(
                        color = if (isLast) NeonLime else NeonCyan,
                        radius = if (isLast) 7.dp.toPx() else 4.5.dp.toPx(),
                        center = pt
                    )
                    drawCircle(
                        color = DarkBackground,
                        radius = if (isLast) 3.5.dp.toPx() else 2.dp.toPx(),
                        center = pt
                    )
                }
            }
        }

        // Timeline labels
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 4.dp),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            val sdf = SimpleDateFormat("MMM d", Locale.getDefault())
            Text(
                text = sdf.format(Date(displayLogs.first().timestamp)),
                color = TextMuted,
                fontSize = 11.sp
            )
            Text(
                text = "Spinal Decompression Trend",
                color = TextSecondary,
                fontSize = 11.sp,
                fontWeight = FontWeight.Medium
            )
            Text(
                text = sdf.format(Date(displayLogs.last().timestamp)),
                color = TextMuted,
                fontSize = 11.sp
            )
        }
    }
}
