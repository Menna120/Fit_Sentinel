package com.example.fit_sentinel.ui.screens.on_boarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ColumnScope
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
fun OnboardingScreenContainer(
    continueEnabled: Boolean,
    skipEnabled: Boolean,
    modifier: Modifier = Modifier,
    showSkipButton: Boolean = false,
    onContinueClick: () -> Unit,
    onSkipClick: () -> Unit,
    content: @Composable (ColumnScope.() -> Unit),
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        content()

        Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
            if (showSkipButton) {
                MainButton(
                    onClick = onSkipClick,
                    text = "Skip",
                    showIcon = false,
                    enabled = skipEnabled
                )
            }

            MainButton(
                onClick = onContinueClick,
                text = "Continue",
                showIcon = false,
                enabled = continueEnabled
            )
        }

    }
}


@Preview(showBackground = true)
@Composable
private fun OnBoardingPreview() {
    Fit_SentinelTheme {
        OnboardingScreenContainer(
            continueEnabled = true,
            skipEnabled = false,
            showSkipButton = true,
            onContinueClick = {},
            onSkipClick = {}) {}
    }
}