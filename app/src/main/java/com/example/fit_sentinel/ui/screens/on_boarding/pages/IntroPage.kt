package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun IntroPage(
    modifier: Modifier = Modifier,
    onStartClick: () -> Unit
) {
    Column(
        modifier = modifier.fillMaxSize(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Spacer(modifier = Modifier.weight(1f))
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Image(
                painter = painterResource(id = R.drawable.intro_image),
                contentDescription = null,
                modifier = Modifier.aspectRatio(1f)
            )
            Text(
                "Get better life with Fit Sentinel",
                color = MaterialTheme.colorScheme.onBackground,
                style = MaterialTheme.typography.headlineMedium,
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(.7f)
            )
            Text(
                "Wa are here to provide solutions to keep your body ideal and maintain yor health.",
                color = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.4f),
                style = MaterialTheme.typography.titleMedium.copy(fontSize = 16.sp),
                textAlign = TextAlign.Center,
                modifier = Modifier.fillMaxWidth(.9f)
            )
        }

        Spacer(modifier = Modifier.weight(1f))
        MainButton(
            text = "Let’s get started",
            enabled = true,
            showIcon = true,
            onClick = onStartClick
        )
    }
}

@Preview(showBackground = true)
@Composable
private fun IntroPagePreview() {
    Fit_SentinelTheme {
        IntroPage(onStartClick = {})
    }
}