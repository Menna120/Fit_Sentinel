package com.example.fit_sentinel.ui.common

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.screens.main.home.components.InfoCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun MonitorRow(
    time: String,
    calories: String,
    distance: String,
    modifier: Modifier = Modifier
) {
    val weight = 1f
    val ratio = .9f

    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        InfoCard(
            R.drawable.time, time, "Time", Modifier
                .weight(weight)
                .aspectRatio(ratio)
        )
        InfoCard(
            R.drawable.calories, calories, "Kcal", Modifier
                .weight(weight)
                .aspectRatio(ratio)
        )
        InfoCard(
            R.drawable.walk, distance, "Km", Modifier
                .weight(weight)
                .aspectRatio(ratio)
        )
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MonitorRowPreview() {
    Fit_SentinelTheme {
        MonitorRow(
            "1h 14min",
            "360",
            "5.64",
            Modifier
                .fillMaxSize()
                .padding(horizontal = 16.dp)
        )
    }
}