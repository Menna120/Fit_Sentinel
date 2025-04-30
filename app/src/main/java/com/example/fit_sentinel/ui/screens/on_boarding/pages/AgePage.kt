package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.ui.screens.on_boarding.components.NumberPicker
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun AgePage(
    selectedAge: Int,
    onAgeChange: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    QuestionContainer("What is your Age?", modifier = modifier.fillMaxSize()) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Spacer(Modifier.weight(.5f))

            NumberPicker(
                value = selectedAge,
                range = 10..100,
                onValueChange = onAgeChange,
                unit = "years"
            )
            Spacer(Modifier.weight(1f))
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun AgePagePreview() {
    var age by remember { mutableIntStateOf(20) }
    Fit_SentinelTheme {
        AgePage(selectedAge = age, onAgeChange = { age = it })
    }
}