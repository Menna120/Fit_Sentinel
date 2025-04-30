package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer

@Composable
fun IllnessPage(
    illnessDescription: String,
    modifier: Modifier = Modifier,
    onDescriptionChange: (String) -> Unit
) {
    QuestionContainer("Do you have any chronic diseases?", modifier = modifier.fillMaxSize()) {
        TextAreaInput(value = illnessDescription, onValueChange = onDescriptionChange)
    }

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TextAreaInput(
    value: String,
    onValueChange: (String) -> Unit,
    modifier: Modifier = Modifier,
    placeholderText: String = "Describe it here",
    enabled: Boolean = true,
    readOnly: Boolean = false,
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        focusedTextColor = MaterialTheme.colorScheme.onBackground,
        unfocusedTextColor = MaterialTheme.colorScheme.onBackground.copy(alpha = .6f),
        focusedIndicatorColor = MaterialTheme.colorScheme.primary,
        unfocusedIndicatorColor = MaterialTheme.colorScheme.primary.copy(alpha = .4f),
        focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.6f),
    )
) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .height(150.dp),
        enabled = enabled,
        readOnly = readOnly,
        textStyle = MaterialTheme.typography.titleMedium,
        placeholder = {
            Text(
                placeholderText,
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp)
            )
        },
        colors = colors,
        shape = shape,
        singleLine = false
    )
}

@Preview(showBackground = true)
@Composable
fun IllnessPagePreview() {
    var description by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
    ) {
        IllnessPage(description) { description = it }
    }
}