package com.example.fit_sentinel.ui.screens.main.settings

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.domain.model.HeightUnit
import com.example.fit_sentinel.domain.model.WeightUnit
import com.example.fit_sentinel.ui.screens.main.settings.components.EditGenderCard
import com.example.fit_sentinel.ui.screens.main.settings.components.EditMeasuresCard
import com.example.fit_sentinel.ui.screens.main.settings.components.EditTextCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

enum class EditableField {
    Name, Age, Gender, Weight, Height, TargetWeight
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SettingLayout(
    name: String,
    onNameChange: (String) -> Unit,
    age: String,
    onAgeChange: (String) -> Unit,
    gender: Gender?,
    onGenderChange: (Gender?) -> Unit,
    weight: String,
    weightUnit: WeightUnit?,
    onWeightChange: (String, WeightUnit?) -> Unit,
    height: String,
    heightUnit: HeightUnit?,
    onHeightChange: (String, HeightUnit?) -> Unit,
    targetWeight: String,
    onTargetWeightChange: (String) -> Unit,
    showDialog: Boolean,
    editingField: EditableField?,
    onDismissDialog: () -> Unit,
    onShowDialog: (EditableField) -> Unit
) {
    Column(
        modifier = Modifier
            .verticalScroll(rememberScrollState())
            .padding(horizontal = 16.dp)
            .fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.SpaceEvenly
    ) {
        Text(
            text = "Change your data",
            color = MaterialTheme.colorScheme.onBackground,
            style = MaterialTheme.typography.titleLarge
        )

        DataEditRow(
            label = "Name",
            value = name,
            onEditClick = { onShowDialog(EditableField.Name) }
        )
        DataEditRow(
            label = "Age",
            value = age,
            onEditClick = { onShowDialog(EditableField.Age) }
        )
        DataEditRow(
            label = "Gender",
            value = gender?.name ?: "",
            onEditClick = { onShowDialog(EditableField.Gender) }
        )
        DataEditRow(
            label = "Weight",
            value = weight,
            onEditClick = { onShowDialog(EditableField.Weight) }
        )
        DataEditRow(
            label = "Height",
            value = height,
            onEditClick = { onShowDialog(EditableField.Height) }
        )
        DataEditRow(
            label = "Target Weight",
            value = targetWeight,
            onEditClick = { onShowDialog(EditableField.TargetWeight) }
        )
    }

    if (showDialog) {
        when (editingField) {
            EditableField.Name -> EditTextCard(
                "User Name",
                currentValue = name,
                onDismiss = onDismissDialog,
                onSubmit = onNameChange
            )

            EditableField.Age -> EditTextCard(
                "Age",
                currentValue = age,
                onDismiss = onDismissDialog,
                onSubmit = onAgeChange
            )

            EditableField.Gender -> EditGenderCard(
                gender,
                onDismiss = onDismissDialog,
                onSubmit = onGenderChange
            )

            EditableField.Weight -> EditMeasuresCard(
                "Weight : ",
                onDismiss = onDismissDialog,
                onSubmit = onWeightChange,
                currentValue = weight,
                units = WeightUnit.entries,
                selectedUnit = weightUnit
            )

            EditableField.Height -> EditMeasuresCard(
                "Height : ",
                onDismiss = onDismissDialog,
                onSubmit = onHeightChange,
                currentValue = height,
                units = HeightUnit.entries,
                selectedUnit = heightUnit
            )

            EditableField.TargetWeight -> EditTextCard(
                "Target Weight",
                currentValue = targetWeight,
                onDismiss = onDismissDialog,
                onSubmit = onTargetWeightChange
            )

            null -> { /* Should not happen */
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
fun PreviewSettingLayout() {
    Fit_SentinelTheme {
        SettingLayout(
            name = "Ahmed Mohamed",
            onNameChange = {},
            age = "28 Year",
            onAgeChange = {},
            gender = Gender.Male,
            onGenderChange = {},
            weight = "65 Kg",
            weightUnit = WeightUnit.Kg,
            onWeightChange = { _, _ -> },
            height = "170 cm",
            heightUnit = HeightUnit.Cm,
            onHeightChange = { _, _ -> },
            targetWeight = "60 Kg",
            onTargetWeightChange = {},
            showDialog = false,
            editingField = null,
            onDismissDialog = {},
            onShowDialog = {}
        )
    }
}
