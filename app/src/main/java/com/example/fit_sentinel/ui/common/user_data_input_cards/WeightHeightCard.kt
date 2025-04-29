package com.example.fit_sentinel.ui.common.user_data_input_cards

import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun WeightHeightCard(
    initialWeight: Float,
    initialHeight: Float,
    onWeightChange: (Float, WeightUnit) -> Unit,
    onHeightChange: (Float, HeightUnit) -> Unit,
    modifier: Modifier = Modifier,
    onDismiss: () -> Unit
) {
    var weightText by remember { mutableStateOf(initialWeight.toString()) }
    var selectedWeightUnit by remember { mutableStateOf(WeightUnit.KG) }

    var heightText by remember { mutableStateOf(initialHeight.toString()) }
    var selectedHeightUnit by remember { mutableStateOf(HeightUnit.CM) }

    LaunchedEffect(weightText, selectedWeightUnit) {
        val weight = weightText.toFloatOrNull() ?: 0f
        onWeightChange(weight, selectedWeightUnit)
    }

    LaunchedEffect(heightText, selectedHeightUnit) {
        val height = heightText.toFloatOrNull() ?: 0f
        onHeightChange(height, selectedHeightUnit)
    }

    Dialog(onDismissRequest = onDismiss) {
        MainCard(modifier = modifier) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(20.dp)
            ) {
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
}


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> MeasurementInputRow(
    label: String,
    units: List<T>,
    selectedUnit: T,
    value: String,
    modifier: Modifier = Modifier,
    onSelectedUnitChange: (T) -> Unit,
    onValueChange: (String) -> Unit
) {
    Column(
        modifier = modifier,
        verticalArrangement = Arrangement.spacedBy(8.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Row(
            modifier = Modifier.padding(2.dp)
        ) {
            units.forEach { unit ->
                UnitToggleChip(
                    text = unit.toString().lowercase(),
                    isSelected = selectedUnit == unit,
                    onClick = { onSelectedUnitChange(unit) }
                )
            }
        }
        Row(
            modifier = Modifier.fillMaxWidth(.7f),
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(label, style = MaterialTheme.typography.titleMedium)

            TextField(
                value = value,
                onValueChange = onValueChange,
                modifier = Modifier.width(120.dp),
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    unfocusedIndicatorColor = MaterialTheme.colorScheme.primary,
                    focusedTextColor = MaterialTheme.colorScheme.onBackground,
                    unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.5f)
                ),
                singleLine = true
            )
        }
    }
}

@Composable
fun UnitToggleChip(
    text: String,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Surface(
        modifier = Modifier
            .clickable(onClick = onClick)
            .padding(horizontal = 8.dp)
            .border(
                1.dp,
                if (!isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary,
                CircleShape
            )
            .clip(CircleShape),
        color = if (isSelected) MaterialTheme.colorScheme.primary else Color.Transparent,
    ) {
        Text(
            text = text,
            color = if (isSelected) MaterialTheme.colorScheme.onPrimary else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.padding(
                horizontal = 12.dp,
                vertical = 6.dp
            ),
            fontSize = 14.sp
        )
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewWeightHeightCard() {
    Fit_SentinelTheme {
        WeightHeightCard(
            initialWeight = 70f,
            initialHeight = 170f,
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
