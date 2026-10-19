package com.example.ui.components

import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.CyanAccent
import com.example.ui.theme.DarkSurfaceVariant
import com.example.ui.theme.LimeAccent
import com.example.ui.theme.TextMuted
import com.example.ui.theme.TextPrimary

@Composable
fun CircularWorkoutTimer(
    totalSeconds: Int,
    remainingSeconds: Int,
    isPlaying: Boolean,
    modifier: Modifier = Modifier,
    size: Dp = 160.dp,
    strokeWidth: Dp = 10.dp
) {
    val progress = if (totalSeconds > 0) remainingSeconds.toFloat() / totalSeconds.toFloat() else 0f
    val animatedProgress by animateFloatAsState(
        targetValue = progress.coerceIn(0f, 1f),
        animationSpec = tween(durationMillis = 500),
        label = "timerProgress"
    )

    Box(
        modifier = modifier.size(size),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(size)) {
            val stroke = Stroke(width = strokeWidth.toPx(), cap = StrokeCap.Round)

            // Background track
            drawCircle(
                color = DarkSurfaceVariant,
                radius = (size.toPx() - strokeWidth.toPx()) / 2f,
                style = stroke
            )

            // Animated progress arc
            drawArc(
                brush = Brush.sweepGradient(
                    colors = listOf(CyanAccent, LimeAccent, CyanAccent)
                ),
                startAngle = -90f,
                sweepAngle = 360f * animatedProgress,
                useCenter = false,
                style = stroke
            )
        }

        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            val mins = remainingSeconds / 60
            val secs = remainingSeconds % 60
            val timeString = String.format("%02d:%02d", mins, secs)

            Text(
                text = timeString,
                style = MaterialTheme.typography.headlineMedium.copy(
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                ),
                color = TextPrimary
            )
            Text(
                text = if (isPlaying) "STRETCHING" else "PAUSED",
                style = MaterialTheme.typography.labelSmall,
                color = if (isPlaying) LimeAccent else TextMuted
            )
        }
    }
}
