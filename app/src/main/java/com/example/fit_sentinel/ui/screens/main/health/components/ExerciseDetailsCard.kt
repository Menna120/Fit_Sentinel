package com.example.fit_sentinel.ui.screens.main.health.components

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun ExerciseDetailsCard(
    text: String,
    value: String,
    @DrawableRes icon: Int,
    iconTint: Color,
    modifier: Modifier = Modifier
) {
    MainCard(modifier = modifier.size(120.dp, 100.dp)) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(vertical = 16.dp, horizontal = 12.dp),
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = text,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 12.sp),
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f)
                )
                Icon(
                    painter = painterResource(id = icon),
                    contentDescription = text,
                    tint = iconTint,
                    modifier = Modifier.size(24.dp)
                )
            }

            Text(
                value,
                style = MaterialTheme.typography.bodyMedium.copy(fontSize = 16.sp),
                color = MaterialTheme.colorScheme.onBackground
            )
        }
    }
}

@Preview
@Composable
private fun ExerciseDetailsCardPreview() {
    Fit_SentinelTheme {
        ExerciseDetailsCard(
            "Exercises",
            "12",
            R.drawable.gym,
            Color.Blue
        )
    }
}