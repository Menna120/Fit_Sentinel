package com.example.fit_sentinel.ui.common.ruler

import android.annotation.SuppressLint
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.material3.LocalTextStyle
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.TextLayoutResult
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.drawText
import androidx.compose.ui.text.rememberTextMeasurer
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.theme.Purple40
import kotlin.math.abs
import kotlin.math.roundToInt

@SuppressLint("UnusedBoxWithConstraintsScope")
@Composable
fun FadingScrollableRuler(
    unit: String,
    minValue: Int,
    maxValue: Int,
    modifier: Modifier = Modifier,
    initialValue: Int = (minValue + maxValue) / 2,
    majorTickHeight: Dp = 100.dp,
    minorTickHeight: Dp = 50.dp,
    majorTickInterval: Int = 5,
    pixelsPerUnitDp: Dp = 15.dp,
    rulerColor: Color = Purple40,
    tickTextColor: Color = Purple40,
    valueTextStyle: TextStyle = LocalTextStyle.current.copy(fontSize = 20.sp),
    tickTextStyle: TextStyle = TextStyle(fontSize = 12.sp, textAlign = TextAlign.Center),
    minTickAlpha: Float = 0.4f,
    fadeWidthDp: Dp = 50.dp,
    centerTickHighlightHeightIncreaseDp: Dp = 15.dp,
    spaceBetweenValueAndRuler: Dp = 12.dp,
    tickTextVerticalOffset: Dp = 4.dp,
    tickStrokeWidth: Dp = 4.dp,
    tickStrokeCap: StrokeCap = StrokeCap.Round,
    onValueChange: (Int) -> Unit
) {
    val density = LocalDensity.current
    val textMeasurer = rememberTextMeasurer()

    val pixelsPerUnit = with(density) { pixelsPerUnitDp.toPx() }
    val majorTickHeightPx = with(density) { majorTickHeight.toPx() }
    val minorTickHeightPx = with(density) { minorTickHeight.toPx() }
    val tickTextVerticalOffsetPx = with(density) { tickTextVerticalOffset.toPx() }
    val fadeWidthPx = with(density) { fadeWidthDp.toPx() }
    val centerTickHighlightHeightIncreasePx =
        with(density) { centerTickHighlightHeightIncreaseDp.toPx() }
    val tickStrokeWidthPx = with(density) { tickStrokeWidth.toPx() }

    val totalRange = maxValue - minValue

    val scrollState = rememberScrollState()

    var centerValue by remember { mutableIntStateOf(initialValue) }

    val maxTickHeightWithHighlightPx = majorTickHeightPx + centerTickHighlightHeightIncreasePx
    val maxTickTextLayoutResult: TextLayoutResult = textMeasurer.measure(
        text = AnnotatedString(maxValue.toString()),
        style = tickTextStyle
    )
    val maxTickTextHeightPx = maxTickTextLayoutResult.size.height

    val requiredRulerAndTextHeightPx =
        maxTickHeightWithHighlightPx + maxTickTextHeightPx + tickTextVerticalOffsetPx

    val requiredRulerPartHeightDp = with(density) { requiredRulerAndTextHeightPx.toDp() }


    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val containerWidthPx = with(density) { maxWidth.toPx() }

        val totalContentWidthPx = totalRange * pixelsPerUnit + containerWidthPx

        LaunchedEffect(scrollState.value, containerWidthPx, pixelsPerUnit, minValue) {
            if (containerWidthPx > 0 && pixelsPerUnit > 0) {
                val centerPixelOffsetInCanvas = scrollState.value + containerWidthPx / 2f
                val calculatedValue =
                    minValue + (centerPixelOffsetInCanvas - containerWidthPx / 2f) / pixelsPerUnit
                centerValue = calculatedValue.roundToInt().coerceIn(minValue, maxValue)
            }
        }

        LaunchedEffect(Unit, initialValue, containerWidthPx, pixelsPerUnit) {
            if (containerWidthPx > 0 && pixelsPerUnit > 0) {
                val initialValuePixelOffsetInCanvas =
                    (initialValue - minValue) * pixelsPerUnit + containerWidthPx / 2f

                val targetScrollPosition = initialValuePixelOffsetInCanvas - (containerWidthPx / 2f)

                scrollState.scrollTo(
                    targetScrollPosition.roundToInt().coerceIn(0, scrollState.maxValue)
                )
            }
        }

        LaunchedEffect(scrollState.isScrollInProgress) {
            if (!scrollState.isScrollInProgress) {
                if (containerWidthPx > 0 && pixelsPerUnit > 0) {
                    val snappedValue = centerValue.coerceIn(minValue, maxValue)
                    val snappedValuePixelOffsetInCanvas =
                        (snappedValue - minValue) * pixelsPerUnit + containerWidthPx / 2f
                    val targetScrollPosition =
                        snappedValuePixelOffsetInCanvas - (containerWidthPx / 2f)

                    scrollState.animateScrollTo(
                        targetScrollPosition.roundToInt().coerceIn(0, scrollState.maxValue)
                    )
                    onValueChange(snappedValue)
                }
            }
        }

        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(spaceBetweenValueAndRuler),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "$centerValue $unit",
                style = valueTextStyle
            )

            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(requiredRulerPartHeightDp)
                    .horizontalScroll(scrollState)
            ) {
                Canvas(
                    modifier = Modifier
                        .fillMaxHeight()
                        .width(with(density) { totalContentWidthPx.toDp() })
                ) {
                    val canvasHeight = size.height
                    val centerOfVisibleAreaPx = scrollState.value + containerWidthPx / 2f

                    val startVisibleX = scrollState.value - fadeWidthPx
                    val endVisibleX = scrollState.value + containerWidthPx + fadeWidthPx

                    val startIndex =
                        (minValue + (startVisibleX - containerWidthPx / 2f) / pixelsPerUnit).roundToInt()
                            .coerceIn(minValue, maxValue)
                    val endIndex =
                        (minValue + (endVisibleX - containerWidthPx / 2f) / pixelsPerUnit).roundToInt()
                            .coerceIn(minValue, maxValue)


                    for (value in startIndex..endIndex) {
                        val xPos = (value - minValue) * pixelsPerUnit + containerWidthPx / 2f
                        val distance = abs(xPos - centerOfVisibleAreaPx)

                        val currentAlpha = if (distance >= fadeWidthPx) {
                            minTickAlpha
                        } else {
                            minTickAlpha + (1.0f - minTickAlpha) * (1.0f - (distance / fadeWidthPx))
                        }

                        val isMajorTick =
                            (majorTickInterval != 0 && value % majorTickInterval == 0) || value == minValue || value == maxValue
                        var tickHeight = if (isMajorTick) majorTickHeightPx else minorTickHeightPx

                        if (value == centerValue) {
                            tickHeight += centerTickHighlightHeightIncreasePx
                        }

                        val currentTickColor = rulerColor.copy(alpha = currentAlpha)
                        val currentTickTextColor = tickTextColor.copy(alpha = currentAlpha)

                        drawLine(
                            color = currentTickColor,
                            start = Offset(x = xPos, y = canvasHeight),
                            end = Offset(x = xPos, y = canvasHeight - tickHeight),
                            cap = tickStrokeCap,
                            strokeWidth = tickStrokeWidthPx
                        )

                        if (isMajorTick) {
                            val textLayoutResult: TextLayoutResult = textMeasurer.measure(
                                text = AnnotatedString(value.toString()),
                                style = tickTextStyle.copy(color = currentTickTextColor)
                            )
                            drawText(
                                textLayoutResult = textLayoutResult,
                                topLeft = Offset(
                                    x = xPos - textLayoutResult.size.width / 2f,
                                    y = canvasHeight - tickHeight - textLayoutResult.size.height - tickTextVerticalOffsetPx
                                )
                            )
                        }
                    }
                }
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewFadingScrollableRuler() {
    MaterialTheme {
        FadingScrollableRuler(
            minValue = 0,
            maxValue = 200,
            unit = "kg"
        ) {}
    }
}