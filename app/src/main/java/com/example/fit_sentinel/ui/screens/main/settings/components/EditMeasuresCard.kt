package com.example.fit_sentinel.ui.screens.main.settings.components

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun <T> EditMeasuresCard(
    label: String,
    onDismiss: () -> Unit,
    onSubmit: (String, T?) -> Unit,
    currentValue: String,
    units: List<T>,
    selectedUnit: T?,
    modifier: Modifier = Modifier
) {
    var value by remember(currentValue) { mutableStateOf(currentValue) }
    var unit by remember(selectedUnit) { mutableStateOf(selectedUnit) }

    SettingEditCardContainer(onDismiss, { onSubmit(value, unit) }, modifier) {
        MeasurementInputRow(
            label = label,
            units = units,
            selectedUnit = unit,
            value = value,
            onSelectedUnitChange = { unit = it },
            onValueChange = { value = it }
        )
    }
}

@Preview
@Composable
private fun EditMeasuresCardPreview() {
    Fit_SentinelTheme {
        EditMeasuresCard(
            "Height : ",
            onDismiss = { },
            onSubmit = { _, _ -> },
            currentValue = "120",
            units = HeightUnit.entries,
            selectedUnit = HeightUnit.Cm
        )
    }
}