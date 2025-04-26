package com.example.fit_sentinel.ui.screens.main.health

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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.onGloballyPositioned
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@Composable
fun ScaleIndicatorBar(
    value: Float,
    categoryLabel: String,
    segments: List<ScaleSegment>,
    minValue: Float,
    maxValue: Float,
    title: String? = null,
    modifier: Modifier = Modifier
) {
    // State to hold the measured width of the colored bar in pixels
    var barWidthPx by remember { mutableStateOf(0) }
    // State to hold the measured width of the indicator label text in pixels
    var indicatorLabelWidthPx by remember { mutableStateOf(0) }

    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            // Optional Title row (can include edit icon etc.)
            title?.let {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Text(text = it, fontSize = 16.sp)
                }
                Spacer(modifier = Modifier.height(8.dp))
            }

            // Category Label and Indicator Box
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(20.dp) // Allocate space for the indicator label
            ) {
                // Calculate the horizontal position of the indicator based on the numerical value
                val indicatorPosition = remember(value, minValue, maxValue, barWidthPx) {
                    if (barWidthPx == 0 || maxValue <= minValue) return@remember 0f
                    val range = maxValue - minValue

                    // Calculate the percentage of the value within the total numerical range
                    val valuePercentage = ((value - minValue) / range).coerceIn(0f, 1f)
                    // Convert the percentage to a pixel position on the bar
                    barWidthPx * valuePercentage
                }

                // The indicator label (Text with background)
                Text(
                    text = categoryLabel,
                    color = Color.Black, // Text color for the label
                    fontSize = 12.sp,
                    modifier = Modifier
                        // Measure the width of the Text composable after layout
                        .onGloballyPositioned { coordinates ->
                            indicatorLabelWidthPx = coordinates.size.width
                        }
                        // Position the label horizontally and align to the bottom-start of the Box
                        .offset {
                            IntOffset(
                                // Calculate x-offset to center the label: position - half_label_width
                                x = indicatorPosition.toInt() - (indicatorLabelWidthPx / 2),
                                y = 0 // No vertical offset needed as aligned to bottom
                            )
                        }
                        .align(Alignment.BottomStart) // Align the calculated offset position relative to the bottom-start of the parent Box
                        // Add styling to the label (background, padding, shape)
                        .background(
                            Color.Green.copy(alpha = 0.7f),
                            RoundedCornerShape(4.dp)
                        ) // Example background color
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                )
            }

            Spacer(modifier = Modifier.height(4.dp)) // Space between indicator and the colored bar

            // The Colored Scale Bar segments
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(24.dp) // Fixed height for the colored bar
                    .clip(RoundedCornerShape(4.dp)) // Apply rounded corners to the bar
                    // Measure the width of the Row (the bar) after layout
                    .onGloballyPositioned { coordinates ->
                        barWidthPx = coordinates.size.width
                    }
            ) {
                var currentThreshold =
                    minValue // Start from the minimum value for range calculation
                val totalRange = maxValue - minValue // Total numerical range of the bar

                // Iterate through the segments and create a Spacer for each
                segments.forEach { segment ->
                    // *** MODIFICATION HERE: Calculate weight based on numerical range ***
                    val segmentRange =
                        segment.maxThreshold - currentThreshold // Numerical width of this segment
                    val segmentWeight =
                        if (totalRange > 0) segmentRange / totalRange else 0f // Proportion of total range

                    Spacer(
                        modifier = Modifier
                            // Assign weight proportional to the numerical range width
                            .weight(segmentWeight.coerceAtLeast(0f)) // Use the calculated weight
                            // Apply the segment's color as background
                            .background(segment.color)
                            // Make the spacer fill the height of the parent Row
                            .fillMaxHeight()
                    )
                    // Update the current threshold for the next segment's calculation
                    currentThreshold = segment.maxThreshold
                }

                // Add a final segment for any range above the last defined maxThreshold up to maxValue
                // This handles cases where maxValue is beyond the last segment's threshold.
                if (currentThreshold < maxValue) {
                    val remainingRange = maxValue - currentThreshold
                    val remainingWeight = if (totalRange > 0) remainingRange / totalRange else 0f
                    Spacer(
                        modifier = Modifier
                            .weight(remainingWeight.coerceAtLeast(0f))
                            .background(
                                segments.lastOrNull()?.color ?: Color.Transparent
                            ) // Use color of the last segment or default
                            .fillMaxHeight()
                    )
                }
            }

            // Optional: Display the current numerical value below the bar
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Value: ${"%.2f".format(value)}",
                fontSize = 14.sp
            ) // Format float to 2 decimal places
        }
    }
}

// Preview function to see the composable in action
@Preview(showBackground = true)
@Composable
fun PreviewScaleIndicatorBarProportionalSegments() {
    // Use the defined bmiSegments list for the preview
    val bmiSegmentsForPreview = bmiSegments

    Column(modifier = Modifier.padding(16.dp)) {
        // Example 1: Healthy Weight
        ScaleIndicatorBar(
            value = 24.22f,
            categoryLabel = "Healthy weight",
            segments = bmiSegmentsForPreview,
            minValue = 17f, // Define the numerical range displayed by the bar
            maxValue = 45f, // Set a reasonable max value to cover common BMI ranges
            title = "BMI (Kg / m2)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Example 2: Severely Obese
        ScaleIndicatorBar(
            value = 37.0f,
            categoryLabel = "Severely Obese",
            segments = bmiSegmentsForPreview,
            minValue = 15f,
            maxValue = 45f,
            title = "BMI (Kg / m2)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Example 3: Morbidly Obese
        ScaleIndicatorBar(
            value = 42.0f,
            categoryLabel = "Morbidly Obese",
            segments = bmiSegmentsForPreview,
            minValue = 15f,
            maxValue = 45f,
            title = "BMI (Kg / m2)",
            modifier = Modifier.fillMaxWidth()
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Example 4: Underweight
        ScaleIndicatorBar(
            value = 17.0f,
            categoryLabel = "Underweight",
            segments = bmiSegmentsForPreview,
            minValue = 15f,
            maxValue = 45f,
            title = "BMI (Kg / m2)",
            modifier = Modifier.fillMaxWidth()
        )
    }
}