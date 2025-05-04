package com.example.fit_sentinel.ui.screens.on_boarding.components

import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun NumberPicker(
    value: Int,
    range: Iterable<Int> = (10..300),
    modifier: Modifier = Modifier,
    label: (Int) -> String = { it.toString() },
    dividersColor: Color = MaterialTheme.colorScheme.primary,
    textStyle: TextStyle = MaterialTheme.typography.titleLarge,
    unit: String? = "cm",
    unitTextStyle: TextStyle = MaterialTheme.typography.bodyMedium,
    onValueChange: (Int) -> Unit,
) {
    ListItemPicker(
        modifier = modifier,
        label = label,
        value = value,
        onValueChange = onValueChange,
        dividersColor = dividersColor,
        list = range.toList(),
        textStyle = textStyle,
        unit = unit,
        unitTextStyle = unitTextStyle
    )
}

@Preview(showBackground = true)
@Composable
private fun NumberPickerPreview() {
    var selectedValue by remember { mutableIntStateOf(270) }
    Fit_SentinelTheme {
        NumberPicker(
            selectedValue,
            onValueChange = { selectedValue = it })
    }
}