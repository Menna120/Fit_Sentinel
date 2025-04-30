package com.example.fit_sentinel.ui.screens.on_boarding.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.AnimationResult
import androidx.compose.animation.core.AnimationVector1D
import androidx.compose.animation.core.DecayAnimationSpec
import androidx.compose.animation.core.calculateTargetValue
import androidx.compose.animation.core.exponentialDecay
import androidx.compose.foundation.background
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProvideTextStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.layout.Layout
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import kotlinx.coroutines.launch
import kotlin.math.abs
import kotlin.math.roundToInt

private fun <T> getItemIndexForOffset(
    range: List<T>,
    value: T,
    offset: Float,
    itemHeightPx: Float
): Int {
    val indexOf = range.indexOf(value) - (offset / itemHeightPx).roundToInt()
    return maxOf(0, minOf(indexOf, range.count() - 1))
}

@Composable
fun <T> ListItemPicker(
    modifier: Modifier = Modifier,
    label: (T) -> String = { it.toString() },
    value: T,
    onValueChange: (T) -> Unit,
    dividersColor: Color = MaterialTheme.colorScheme.primary,
    list: List<T>,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium.copy(fontSize = 35.sp),
    unit: String? = null,
    unitTextStyle: TextStyle = MaterialTheme.typography.titleLarge,
    visibleItems: Int = 7 // Make visibleItems a parameter
) {
    require(visibleItems >= 1 && visibleItems % 2 != 0) { "visibleItems must be an odd number greater than or equal to 1" }

    val verticalMargin = 8.dp
    // The height of a single item 'slot'
    val itemHeight = 42.dp // Approximate height of a single text item

    val itemHeightPx = with(LocalDensity.current) { itemHeight.toPx() }
    val halfVisibleItems = visibleItems / 2

    val coroutineScope = rememberCoroutineScope()

    val animatedOffset = remember { Animatable(0f) }
        .apply {
            val index = list.indexOf(value)
            val offsetRange = remember(value, list, itemHeightPx) {
                -((list.count() - 1) - index) * itemHeightPx to
                        index * itemHeightPx
            }
            updateBounds(offsetRange.first, offsetRange.second)
        }

    val coercedAnimatedOffset = animatedOffset.value % itemHeightPx
    val centeredAnimatedOffset = if (coercedAnimatedOffset > itemHeightPx / 2) {
        coercedAnimatedOffset - itemHeightPx
    } else if (coercedAnimatedOffset < -itemHeightPx / 2) {
        coercedAnimatedOffset + itemHeightPx
    } else {
        coercedAnimatedOffset
    }


    val indexOfElement =
        getItemIndexForOffset(list, value, animatedOffset.value, itemHeightPx)

    var dividersWidth by remember { mutableStateOf(0.dp) }

    // Denominator for alpha calculation normalization.
    val alphaNormalization = itemHeightPx * (halfVisibleItems + 1)


    Layout(
        modifier = modifier
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { deltaY ->
                    coroutineScope.launch {
                        animatedOffset.snapTo(animatedOffset.value + deltaY)
                    }
                },
                onDragStopped = { velocity ->
                    coroutineScope.launch {
                        val endValue = animatedOffset.fling(
                            initialVelocity = velocity,
                            animationSpec = exponentialDecay(frictionMultiplier = 20f),
                            adjustTarget = { target ->
                                val coercedTarget = target % itemHeightPx
                                val coercedAnchors =
                                    listOf(
                                        -itemHeightPx,
                                        0f,
                                        itemHeightPx
                                    )
                                val coercedPoint =
                                    coercedAnchors.minByOrNull { abs(it - coercedTarget) }!!
                                val base =
                                    itemHeightPx * (target / itemHeightPx).toInt()
                                coercedPoint + base
                            }
                        ).endState.value

                        val result = list.elementAt(
                            getItemIndexForOffset(list, value, endValue, itemHeightPx)
                        )
                        onValueChange(result)
                        // Snap to the position of the selected item, then animate to center
                        val targetOffset =
                            (list.indexOf(result) - list.indexOf(value)).toFloat() * itemHeightPx
                        animatedOffset.snapTo(animatedOffset.value + targetOffset + animatedOffset.value % itemHeightPx)
                        animatedOffset.animateTo(0f)
                    }
                }
            )
            .padding(vertical = verticalMargin), // Reduced vertical padding
        content = {
            val divideModifier = Modifier
                .width(dividersWidth)
                .height(2.dp)
                .clip(CircleShape)
                .background(color = dividersColor)

            Box(divideModifier)

            Box(
                modifier = Modifier
                    .offset {
                        IntOffset(
                            x = 0,
                            y = centeredAnimatedOffset.roundToInt()
                        )
                    }
                    .padding(horizontal = 16.dp),
                contentAlignment = Alignment.Center
            ) {
                val baseLabelModifier = Modifier.align(Alignment.Center)
                ProvideTextStyle(textStyle) {
                    // Items above the centered one
                    for (i in halfVisibleItems downTo 1) {
                        if (indexOfElement - i >= 0) {
                            // Calculate distance from the layout center
                            val distance =
                                abs(-itemHeightPx * i + centeredAnimatedOffset)
                            // Calculate alpha based on distance: closer items have higher alpha
                            val itemAlpha = (1f - (distance / alphaNormalization)).coerceIn(0f, 1f)
                            Label(
                                text = label(list.elementAt(indexOfElement - i)),
                                modifier = baseLabelModifier
                                    .offset(y = -itemHeight * i)
                                    .alpha(itemAlpha),
                            )
                        }
                    }

                    // Centered item (always fully opaque)
                    Row(
                        modifier = baseLabelModifier.alpha(1f),
                        horizontalArrangement = Arrangement.spacedBy(4.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Label(
                            text = label(list.elementAt(indexOfElement)),
                            modifier = Modifier
                        )
                        if (unit != null) {
                            ProvideTextStyle(unitTextStyle) {
                                Text(
                                    text = unit,
                                    modifier = Modifier
                                        .align(Alignment.Bottom)
                                        .padding(start = 4.dp)
                                )
                            }
                        }
                    }

                    // Items below the centered one
                    for (i in 1..halfVisibleItems) {
                        if (indexOfElement + i < list.count()) {
                            // Calculate distance from the layout center
                            val distance =
                                abs(itemHeightPx * i + centeredAnimatedOffset)
                            // Calculate alpha based on distance: closer items have higher alpha
                            val itemAlpha = (1f - (distance / alphaNormalization)).coerceIn(0f, 1f)
                            Label(
                                text = label(list.elementAt(indexOfElement + i)),
                                modifier = baseLabelModifier
                                    .offset(y = itemHeight * i)
                                    .alpha(itemAlpha)
                            )
                        }
                    }
                }
            }

            Box(divideModifier)
        }
    ) { measurables, constraints ->
        val placeables = measurables.map { measurable ->
            measurable.measure(constraints)
        }

        dividersWidth = placeables
            .drop(1) // Skip the first divider
            .first() // Get the central content Box
            .width
            .toDp()

        // Calculate the total height based on the visible items and vertical margin
        val totalHeight = (itemHeight * visibleItems + verticalMargin * 2).toPx().toInt()


        layout(
            dividersWidth.toPx().toInt(),
            totalHeight
        ) {
            val dividerHeight = placeables.first().height
            val contentBoxHeight = placeables[1].height

            // Place the first divider
            placeables.first().placeRelative(
                x = 0,
                y = (totalHeight / 2 - dividerHeight / 2 - (itemHeight / 2).toPx()).roundToInt()
            )

            // Place the central content box
            placeables[1].placeRelative(x = 0, y = (totalHeight / 2 - contentBoxHeight / 2))

            // Place the second divider
            placeables.last().placeRelative(
                x = 0,
                y = (totalHeight / 2 - dividerHeight / 2 + (itemHeight / 2).toPx()).roundToInt()
            )
        }
    }
}

@Composable
private fun Label(text: String, modifier: Modifier) {
    Text(
        modifier = modifier.pointerInput(Unit) {
            detectTapGestures(onLongPress = {
                // FIXME: Empty to disable text selection
            })
        },
        text = text,
        textAlign = TextAlign.Center,
    )
}

private suspend fun Animatable<Float, AnimationVector1D>.fling(
    initialVelocity: Float,
    animationSpec: DecayAnimationSpec<Float>,
    adjustTarget: ((Float) -> Float)?,
    block: (Animatable<Float, AnimationVector1D>.() -> Unit)? = null,
): AnimationResult<Float, AnimationVector1D> {
    val targetValue = animationSpec.calculateTargetValue(value, initialVelocity)
    val adjustedTarget = adjustTarget?.invoke(targetValue)
    return if (adjustedTarget != null) {
        animateTo(
            targetValue = adjustedTarget,
            initialVelocity = initialVelocity,
            block = block
        )
    } else {
        animateDecay(
            initialVelocity = initialVelocity,
            animationSpec = animationSpec,
            block = block,
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ListItemPickerPreview() {
    Fit_SentinelTheme {
        var selectedValue by remember { mutableIntStateOf(25) }
        ListItemPicker(
            list = (1..100).toList(),
            value = selectedValue,
            onValueChange = { selectedValue = it },
            unit = "cm",
        )
    }
}