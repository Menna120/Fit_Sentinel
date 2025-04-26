package com.example.fit_sentinel.ui.screens.on_boarding

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OnboardingProgressBar(
    viewModel: OnboardingViewModel = viewModel(),
    onBackClick: () -> Unit
) {
    val currentStep by viewModel.currentStep.collectAsState()
    val totalSteps by viewModel.totalSteps.collectAsState()

    val progress = if (totalSteps > 0) currentStep.toFloat() / totalSteps.toFloat() else 0f

    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        val thickness = 16.dp

        IconButton(onBackClick) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                tint = MaterialTheme.colorScheme.onBackground
            )
        }

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
            contentAlignment = Alignment.CenterStart
        ) {
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(CircleShape),
                thickness = thickness,
                color = MaterialTheme.colorScheme.primary.copy(alpha = 0.2f)
            )
            HorizontalDivider(
                modifier = Modifier
                    .fillMaxWidth(progress)
                    .clip(CircleShape),
                thickness = thickness,
                color = MaterialTheme.colorScheme.primary
            )

        }

        Text(
            text = "$currentStep / $totalSteps",
            style = MaterialTheme.typography.titleLarge
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun OnboardingProgressBarPreview() {
    Fit_SentinelTheme {
        OnboardingProgressBar {}
    }
}