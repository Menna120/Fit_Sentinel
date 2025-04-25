package com.example.fit_sentinel.ui.screens.main.reports.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun TotalStepsCard(
    totalSteps: Double,
    modifier: Modifier = Modifier
) {
    MainCard(modifier = modifier) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                painter = painterResource(R.drawable.run),
                contentDescription = "run",
                modifier = Modifier.size(72.dp),
                tint = MaterialTheme.colorScheme.primary
            )

            Spacer(modifier = Modifier.size(32.dp))

            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Text(
                    totalSteps.toString(),
                    color = MaterialTheme.colorScheme.onBackground,
                    fontSize = 40.sp,
                    fontWeight = FontWeight.Bold,
                    style = MaterialTheme.typography.bodyLarge
                )
                Text(
                    "Total steps you have achieved",
                    color = MaterialTheme.colorScheme.onBackground.copy(alpha = .5f),
                    style = MaterialTheme.typography.labelSmall
                )
            }
        }
    }
}

@Preview
@Composable
private fun TotalStepsCardPreview() {
    Fit_SentinelTheme {
        TotalStepsCard(247.345)
    }
}