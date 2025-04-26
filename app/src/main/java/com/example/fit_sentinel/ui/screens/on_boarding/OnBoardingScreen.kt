package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun OnboardingScreen() {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        OnboardingProgressBar { }


        MainButton(
            onClick = { /* Handle Continue Click */ },
            text = "Continue",
            showIcon = false,
            enabled = false
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingPreview() {
    Fit_SentinelTheme {
        OnboardingScreen()
    }
}