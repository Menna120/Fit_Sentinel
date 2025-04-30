package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.ui.screens.on_boarding.components.IconTextInputField
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer

@Composable
fun NamePage(
    name: String,
    modifier: Modifier = Modifier,
    onNameChange: (String) -> Unit
) {
    QuestionContainer(modifier = modifier.fillMaxSize(), question = "What is your name?") {
        IconTextInputField(
            value = name,
            onValueChange = onNameChange,
            placeholderText = "Name"
        )
    }
}

@Preview(showBackground = true)
@Composable
fun NamePagePreview() {
    var name by remember { mutableStateOf("") }
    NamePage(name = name, onNameChange = { name = it })
}