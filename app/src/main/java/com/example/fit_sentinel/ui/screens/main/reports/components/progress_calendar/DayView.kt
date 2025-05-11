package com.example.fit_sentinel.ui.screens.main.reports.components.progress_calendar

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.screens.main.reports.model.DayProgress
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import com.example.fit_sentinel.ui.theme.Green

@Composable
fun DayView(
    dayProgress: DayProgress,
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .padding(4.dp)
            .aspectRatio(1f)
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val size = minOf(size.width, size.height) * 0.8f
            val strokeWidth = 6.dp.toPx()

            drawCircle(
                color = Black.copy(alpha = 0.2f),
                radius = size / 2f,
                center = center,
                style = Stroke(strokeWidth)
            )

            drawArc(
                color = Green,
                startAngle = -90f,
                sweepAngle = -360f * dayProgress.progress,
                useCenter = false,
                size = androidx.compose.ui.geometry.Size(size, size),
                topLeft = androidx.compose.ui.geometry.Offset(
                    (this.size.width - size) / 2f,
                    (this.size.height - size) / 2f
                ),
                style = Stroke(strokeWidth, cap = StrokeCap.Round)
            )
        }

        Text(
            text = dayProgress.dayOfMonth.toString(),
            style = MaterialTheme.typography.titleMedium,
            color = Black
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun DayViewPreview() {
    Fit_SentinelTheme {
        DayView(
            DayProgress(1, .3f),
            Modifier.size(50.dp)
        )
    }
}