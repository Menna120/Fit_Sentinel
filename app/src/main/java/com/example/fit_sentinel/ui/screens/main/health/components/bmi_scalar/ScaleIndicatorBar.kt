package com.example.fit_sentinel.ui.screens.main.health.components.bmi_scalar

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.luminance
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun ScaleIndicatorBar(
    value: Float,
    categoryLabel: String,
    modifier: Modifier = Modifier,
    minValue: Float = 15f,
    maxValue: Float = 45f,
    onEditClick: () -> Unit
) {
    var barWidthPx by remember { mutableIntStateOf(0) }
    var indicatorLabelWidthPx by remember { mutableIntStateOf(0) }

    val indicatorColor = remember(value) {
        bmiSegments.firstOrNull { value <= it.maxThreshold }?.color ?: Color.Gray
    }

    MainCard(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation()
    ) {
        Column(
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            val formattedBmi = "%.2f".format(value)
            val styledText = buildAnnotatedString {
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.primary)) {
                    append("BMI (Kg / m2) : ")
                }
                withStyle(style = SpanStyle(color = MaterialTheme.colorScheme.onBackground)) {
                    append(formattedBmi)
                }
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = styledText,
                    style = MaterialTheme.typography.bodyMedium,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
                )

                IconButton(onClick = onEditClick) {
                    Icon(
                        painter = painterResource(R.drawable.edit),
                        contentDescription = "",
                        modifier = Modifier.size(24.dp)
                    )
                }
            }


            HorizontalDivider()

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                val indicatorPosition = remember(value, minValue, maxValue, barWidthPx) {
                    if (barWidthPx == 0 || maxValue <= minValue) return@remember 0f
                    val range = maxValue - minValue
                    val valuePercentage = ((value - minValue) / range).coerceIn(0f, 1f)
                    barWidthPx * valuePercentage
                }

                Text(
                    text = categoryLabel,
                    color = if (indicatorColor.luminance() > 0.5f) Color.Black else Color.White,
                    style = MaterialTheme.typography.labelSmall,
                    modifier = Modifier
                        .onGloballyPositioned { coordinates ->
                            indicatorLabelWidthPx = coordinates.size.width
                        }
                        .offset {
                            IntOffset(
                                x = indicatorPosition.toInt() - (indicatorLabelWidthPx / 2),
                                y = 0
                            )
                        }
                        .align(Alignment.CenterStart)
                        .background(
                            color = indicatorColor,
                            shape = PointerShape(
                                cornerRadius = 8.dp.toPx(),
                                pointerSize = Size(width = 16.dp.toPx(), height = 8.dp.toPx()),
                            )
                        )
                        .height(28.dp)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp)
                    .padding(horizontal = 16.dp)
                    .clip(RoundedCornerShape(4.dp))
                    .onGloballyPositioned { coordinates ->
                        barWidthPx = coordinates.size.width
                    }
            ) {
                var currentThreshold = minValue
                val totalRange = maxValue - minValue

                bmiSegments.forEach { segment ->
                    val segmentRange = segment.maxThreshold - currentThreshold
                    val segmentWeight = if (totalRange > 0) segmentRange / totalRange else 0f

                    Spacer(
                        modifier = Modifier
                            .weight(segmentWeight.coerceAtLeast(0f))
                            .background(segment.color)
                            .fillMaxHeight()
                    )
                    currentThreshold = segment.maxThreshold
                }

                if (currentThreshold < maxValue) {
                    val remainingRange = maxValue - currentThreshold
                    val remainingWeight = if (totalRange > 0) remainingRange / totalRange else 0f
                    Spacer(
                        modifier = Modifier
                            .weight(remainingWeight.coerceAtLeast(0f))
                            .background(bmiSegments.lastOrNull()?.color ?: Color.Transparent)
                            .fillMaxHeight()
                    )
                }
            }
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewScaleIndicatorBarProportionalSegments() {
    Fit_SentinelTheme {
        ScaleIndicatorBar(
            value = 24.22f,
            categoryLabel = "Healthy weight",
            modifier = Modifier.fillMaxWidth(),
        ) {}
    }
}
