package com.example.fit_sentinel.ui.common.monitor_data

import androidx.annotation.DrawableRes
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import com.example.fit_sentinel.ui.theme.LightGray

@Composable
fun MonitorCard(
    @DrawableRes icon: Int,
    value: String,
    unit: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.aspectRatio(.8f),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.background),
        elevation = CardDefaults.cardElevation(8.dp),
        border = BorderStroke(1.dp, LightGray)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            verticalArrangement = Arrangement.SpaceEvenly,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Icon(
                painterResource(icon),
                "$value $unit",
                Modifier.size(32.dp),
                Color.Unspecified
            )
            Text(
                value,
                textAlign = TextAlign.Center,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(unit, Modifier.alpha(.5f))
        }
    }
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun MonitorCardPreview() {
    Fit_SentinelTheme {
        MonitorCard(
            R.drawable.time,
            "1h 14min", "Time",
            Modifier.size(150.dp)
        )
    }
}