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
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun TargetWeightPage(
    targetWeight: String,
    targetWeightPlaceholder: String,
    weightUnit: String,
    modifier: Modifier = Modifier,
    onTargetWeightChange: (String) -> Unit
) {
    QuestionContainer(modifier = modifier.fillMaxSize(), question = "What is your target weight?") {
        IconTextInputField(
            value = targetWeight,
            onValueChange = onTargetWeightChange,
            placeholderText = "$targetWeightPlaceholder $weightUnit"
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun TargetWeightPagePreview() {
    var targetWeight by remember { mutableStateOf("") }

    Fit_SentinelTheme {
        TargetWeightPage(
            targetWeight = targetWeight,
            targetWeightPlaceholder = "50",
            weightUnit = "kg",
            onTargetWeightChange = { targetWeight = it }
        )
    }
}