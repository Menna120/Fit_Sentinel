package com.example.fit_sentinel.ui.common.user_data_input_cards

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.ui.screens.main.settings.components.MeasurementInputRow
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun WeightHeightCard(
    initialWeight: Float,
    initialHeight: Int,
    onWeightChange: (Float, WeightUnit) -> Unit,
    onHeightChange: (Int, HeightUnit) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var weightText by remember { mutableStateOf(initialWeight.toString()) }
    var selectedWeightUnit by remember { mutableStateOf(WeightUnit.Kg) }

    var heightText by remember { mutableStateOf(initialHeight.toString()) }
    var selectedHeightUnit by remember { mutableStateOf(HeightUnit.Cm) }

    LaunchedEffect(weightText, selectedWeightUnit) {
        val weight = weightText.toFloatOrNull() ?: 0f
        onWeightChange(weight, selectedWeightUnit)
    }

    LaunchedEffect(heightText, selectedHeightUnit) {
        val height = heightText.toIntOrNull() ?: 0
        onHeightChange(height, selectedHeightUnit)
    }

    EditDataCardContainer(onDismiss, modifier) {
        Column(verticalArrangement = Arrangement.spacedBy(32.dp)) {
            MeasurementInputRow(
                label = "Weight :",
                units = WeightUnit.entries,
                selectedUnit = selectedWeightUnit,
                value = weightText,
                onSelectedUnitChange = { unit ->
                    selectedWeightUnit = unit
                },
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '.' }) weightText =
                        newValue
                }
            )

            MeasurementInputRow(
                label = "Height :",
                units = HeightUnit.entries,
                selectedUnit = selectedHeightUnit,
                value = heightText,
                onSelectedUnitChange = { unit ->
                    selectedHeightUnit = unit
                },
                onValueChange = { newValue ->
                    if (newValue.all { it.isDigit() || it == '.' }) heightText =
                        newValue
                }
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeightHeightCard() {
    Fit_SentinelTheme {
        WeightHeightCard(
            initialWeight = 70f,
            initialHeight = 170,
            onWeightChange = { weight, unit ->
                println("Weight changed to: $weight ${unit.name}")
            },
            onHeightChange = { height, unit ->
                println("Height changed to: $height ${unit.name}")
            },
            onDismiss = {}
        )
    }
}
