package com.example.fit_sentinel.ui.screens.main.health

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Rect
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Outline
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Density
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.LayoutDirection
import androidx.compose.ui.unit.dp

class PointerShape(
    private val cornerRadius: Float,
    private val pointerSize: Size,
    private val pointerOffset: Float = 0f
) : Shape {

    override fun createOutline(
        size: Size,
        layoutDirection: LayoutDirection,
        density: Density
    ): Outline {
        val width = size.width
        val height = size.height

        val rectHeight = height - pointerSize.height

        val path = Path().apply {
            moveTo(0f, cornerRadius)
            arcTo(
                rect = Rect(0f, 0f, cornerRadius * 2, cornerRadius * 2),
                startAngleDegrees = 180f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(width - cornerRadius, 0f)
            arcTo(
                rect = Rect(width - cornerRadius * 2, 0f, width, cornerRadius * 2),
                startAngleDegrees = 270f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )
            lineTo(width, rectHeight - cornerRadius)

            arcTo(
                rect = Rect(
                    width - cornerRadius * 2,
                    rectHeight - cornerRadius * 2,
                    width,
                    rectHeight
                ),
                startAngleDegrees = 0f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            val bottomCenterX = width / 2f + pointerOffset

            lineTo(bottomCenterX + pointerSize.width / 2f, rectHeight)
            lineTo(bottomCenterX, height)
            lineTo(bottomCenterX - pointerSize.width / 2f, rectHeight)
            lineTo(cornerRadius, rectHeight)

            arcTo(
                rect = Rect(
                    0f,
                    rectHeight - cornerRadius * 2,
                    cornerRadius * 2,
                    rectHeight
                ),
                startAngleDegrees = 90f,
                sweepAngleDegrees = 90f,
                forceMoveTo = false
            )

            close()
        }

        return Outline.Generic(path)
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewPointerShape() {
    Box(
        modifier = Modifier
            .padding(32.dp)
            .size(120.dp, 80.dp)
            .background(
                color = Color.Green,
                shape = PointerShape(
                    cornerRadius = 8.dp.toPx(),
                    pointerSize = Size(width = 16.dp.toPx(), height = 8.dp.toPx()),
                    pointerOffset = 0.dp.toPx()
                )
            )
            .padding(16.dp),
        contentAlignment = Alignment.Center
    ) {
        Text("Healthy weight", color = Color.White)
    }
}

@Composable
fun Dp.toPx(): Float {
    return with(LocalDensity.current) { this@toPx.toPx() }
}
