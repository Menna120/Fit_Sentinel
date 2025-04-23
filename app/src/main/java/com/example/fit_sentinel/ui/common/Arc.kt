package com.example.fit_sentinel.ui.common

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxScope
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun Arc(
    currentValue: Int,
    targetValue: Int,
    modifier: Modifier = Modifier,
    arcWidth: Dp = 48.dp,
    startAngle: Float = 135f,
    totalAngle: Float = 270f,
    arcBackgroundColor: Color = Black.copy(alpha = 0.15f),
    arcProgressColor: Color = MaterialTheme.colorScheme.primary,
    content: @Composable (BoxScope.() -> Unit)
) {
    Box(
        modifier = modifier.aspectRatio(1f),
        contentAlignment = Alignment.Center
    ) {
        Canvas(
            modifier = Modifier
                .fillMaxSize()
                .padding(arcWidth / 2)
        ) {
            val sweepAngle =
                (currentValue.toFloat() / targetValue.toFloat()).coerceIn(0f, 1f) * 360f

            drawArc(
                color = arcBackgroundColor,
                startAngle = startAngle,
                sweepAngle = totalAngle,
                useCenter = false,
                style = androidx.compose.ui.graphics.drawscope.Stroke(
                    arcWidth.toPx(),
                    cap = StrokeCap.Round
                )
            )

            if (currentValue > 0) {
                drawArc(
                    color = arcProgressColor,
                    startAngle = startAngle,
                    sweepAngle = sweepAngle.coerceAtMost(totalAngle),
                    useCenter = false,
                    style = androidx.compose.ui.graphics.drawscope.Stroke(
                        arcWidth.toPx(),
                        cap = StrokeCap.Round
                    )
                )
            }
        }

        content()
    }
}

@Preview(showBackground = true)
@Composable
private fun ArcPreview() {
    Fit_SentinelTheme {
        Arc(3000, 10000) {}
    }
}