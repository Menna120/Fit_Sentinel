package com.example.fit_sentinel.ui.screens.main.health

import androidx.compose.ui.graphics.Color

data class ScaleSegment(
    val color: Color,
    val maxThreshold: Float
)

val bmiSegments = listOf(
    ScaleSegment(color = Color(0xFF91E1D7), maxThreshold = 18.5f),
    ScaleSegment(color = Color(0xFF4C6C92), maxThreshold = 25f),
    ScaleSegment(color = Color(0xFF61FD67), maxThreshold = 30f),
    ScaleSegment(color = Color(0xFFE7F266), maxThreshold = 35f),
    ScaleSegment(color = Color(0xFFFCA115), maxThreshold = 40f),
    ScaleSegment(color = Color(0xFFFE0011), maxThreshold = 45f)
)