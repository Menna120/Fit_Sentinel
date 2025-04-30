package com.example.fit_sentinel.ui.screens.on_boarding.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.size
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun QuestionContainer(
    question: String,
    modifier: Modifier = Modifier,
    content: @Composable () -> Unit
) {
    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text("Personalized Program", style = MaterialTheme.typography.titleLarge)

        Spacer(modifier = Modifier.size(16.dp))

        Text(
            "Before starting, please answer a few simple questions.",
            style = MaterialTheme.typography.titleLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f)
        )

        Spacer(modifier = Modifier.size(32.dp))

        Column(
            modifier = Modifier.fillMaxSize(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(64.dp)
        ) {
            Text(question, style = MaterialTheme.typography.headlineMedium.copy(fontSize = 26.sp))
            content()
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingContainerPreview() {
    Fit_SentinelTheme {
        QuestionContainer("What is your name?") {}
    }
}