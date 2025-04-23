package com.example.fit_sentinel.ui.screens.main.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import java.time.LocalDate
import java.time.format.DateTimeFormatter

@Composable
fun MonthNavigationHeader(
    currentDate: LocalDate,
    onPreviousMonth: () -> Unit,
    onNextMonth: () -> Unit,
    modifier: Modifier = Modifier,
    dateTextColor: Color = MaterialTheme.colorScheme.onBackground,
) {
    Row(
        modifier = modifier,
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        IconButton(onClick = onPreviousMonth) {
            Icon(
                painter = painterResource(R.drawable.down_arrow),
                contentDescription = "Previous Month"
            )
        }

        Text(
            text = currentDate.format(DateTimeFormatter.ofPattern("MMMM dd, yyyy")),
            style = MaterialTheme.typography.titleLarge,
            color = dateTextColor
        )

        IconButton(onClick = onNextMonth) {
            Icon(
                painter = painterResource(R.drawable.up_arrow),
                contentDescription = "Next Month"
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MonthNavigationHeaderPreview() {
    Fit_SentinelTheme {
        MonthNavigationHeader(LocalDate.now(), {}, {})
    }
}