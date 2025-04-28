package com.example.fit_sentinel.ui.screens.main.health.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Blue
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import com.example.fit_sentinel.ui.theme.Red

@Composable
fun ExerciseDetailsRow(
    reps: Int,
    timesPerWeek: Int,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceAround,
        verticalAlignment = Alignment.CenterVertically
    ) {
        ExerciseDetailsCard("Exercises", "$reps", R.drawable.gym, iconTint = Blue)

        ExerciseDetailsCard(
            "Schedule",
            "$timesPerWeek * 1 Week",
            R.drawable.schedule,
            iconTint = Red
        )
    }
}

@Preview
@Composable
private fun ExerciseDetailsRowPreview() {
    Fit_SentinelTheme {
        ExerciseDetailsRow(reps = 12, timesPerWeek = 3)
    }
}