package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.ui.screens.on_boarding.components.NumberPicker
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun <T> HeightPage(
    selectedUnit: T,
    units: List<T>,
    selectedHeightValue: Int,
    onValueChange: (Int) -> Unit,
    onUnitSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    QuestionContainer("What is your weight?", modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            UnitSelectionRow<T>(
                selectedUnit = selectedUnit,
                units = units,
                onUnitSelected = onUnitSelected
            )

            Spacer(Modifier.weight(1f))

            NumberPicker(
                selectedHeightValue,
                onValueChange = onValueChange,
                unit = selectedUnit.toString().lowercase()
            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HeightPagePreview() {
    Fit_SentinelTheme {
        HeightPage<HeightUnit>(
            selectedUnit = HeightUnit.Cm,
            units = HeightUnit.entries,
            selectedHeightValue = 170,
            onValueChange = {},
            onUnitSelected = {}
        )
    }
}