package com.example.fit_sentinel.ui.screens.main.settings.components

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import com.example.fit_sentinel.domain.model.Gender

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun EditGenderCard(
    currentGender: Gender?,
    onDismiss: () -> Unit,
    onSubmit: (Gender?) -> Unit,
    modifier: Modifier = Modifier
) {
    val gender = Gender.entries
    var selectedGender by remember(currentGender) { mutableStateOf(currentGender) }
    var expanded by remember { mutableStateOf(false) }

    SettingEditCardContainer(onDismiss, { onSubmit(selectedGender) }, modifier) {
        ExposedDropdownMenuBox(
            expanded = expanded,
            onExpandedChange = { expanded = !expanded }
        ) {
            OutlinedTextField(
                value = selectedGender?.name ?: "",
                onValueChange = {},
                readOnly = true,
                label = { Text("Gender") },
                textStyle = MaterialTheme.typography.titleMedium,
                trailingIcon = {
                    ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded)
                },
                modifier = Modifier.fillMaxWidth(),
                shape = CircleShape
            )

            ExposedDropdownMenu(
                expanded = expanded,
                onDismissRequest = { expanded = false }
            ) {
                gender.forEach { gender ->
                    DropdownMenuItem(
                        text = { Text(gender.name, style = MaterialTheme.typography.titleMedium) },
                        onClick = {
                            selectedGender = gender
                            expanded = false
                        }
                    )
                }
            }
        }
    }
}