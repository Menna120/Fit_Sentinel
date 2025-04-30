package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.ui.common.FadingScrollableRuler
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun <T> WeightPage(
    selectedUnit: T,
    units: List<T>,
    selectedWeightValue: Int,
    modifier: Modifier = Modifier,
    minValue: Int = 30,
    maxValue: Int = 300,
    onUnitSelected: (T) -> Unit,
    onValueChange: (Int) -> Unit
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
            Spacer(Modifier.size(32.dp))
            FadingScrollableRuler(
                minValue = minValue,
                maxValue = maxValue,
                initialValue = selectedWeightValue,
                unit = selectedUnit.toString().lowercase(),
                onValueChange = onValueChange
            )
        }
    }
}

@Composable
fun UnitSelectionCard(
    unit: String,
    isSelected: Boolean,
    onCardClick: (unit: String) -> Unit,
    modifier: Modifier = Modifier,
    selectedColor: Color = MaterialTheme.colorScheme.primary,
    defaultBackgroundColor: Color = MaterialTheme.colorScheme.background,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    defaultTextColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val backgroundColor by animateColorAsState(
        if (isSelected) selectedColor else defaultBackgroundColor,
        label = "backgroundColor"
    )

    val textColor by animateColorAsState(
        if (isSelected) selectedTextColor else defaultTextColor,
        label = "textColor"
    )

    Box(
        modifier = modifier
            .padding(horizontal = 8.dp)
            .border(
                1.dp,
                if (!isSelected) MaterialTheme.colorScheme.primary.copy(alpha = 0.2f) else MaterialTheme.colorScheme.primary,
                RoundedCornerShape(20.dp)
            )
            .clip(RoundedCornerShape(20.dp))
            .size(64.dp)
            .background(backgroundColor)
            .clickable(onClick = { onCardClick(unit) }),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = unit.lowercase(),
            color = textColor,
            style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp)
        )
    }
}

@Composable
fun <T> UnitSelectionRow(
    selectedUnit: T,
    units: List<T>,
    onUnitSelected: (T) -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        UnitSelectionCard(
            unit = units[0].toString(),
            isSelected = units[0] == selectedUnit,
            onCardClick = { onUnitSelected(units[0]) },
        )

        Spacer(Modifier.size(8.dp))

        UnitSelectionCard(
            unit = units[1].toString(),
            isSelected = units[1] == selectedUnit,
            onCardClick = { onUnitSelected(units[1]) },
        )
    }
}

@Preview(showBackground = true)
@Composable
fun WeightPagePreview() {
    var selectedWeightUnit by remember { mutableStateOf<WeightUnit>(WeightUnit.Kg) }
    var selectedWeightValue by remember { mutableIntStateOf(50) }

    Fit_SentinelTheme {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            WeightPage(
                selectedUnit = selectedWeightUnit,
                units = WeightUnit.entries,
                selectedWeightValue = selectedWeightValue,
                onUnitSelected = { unit -> selectedWeightUnit = unit },
                onValueChange = { selectedWeightValue = it })
        }
    }
}