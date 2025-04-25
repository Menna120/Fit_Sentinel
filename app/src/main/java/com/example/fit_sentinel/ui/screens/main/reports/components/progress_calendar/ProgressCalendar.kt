package com.example.fit_sentinel.ui.screens.main.reports.components.progress_calendar

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.screens.main.reports.model.DayProgress
import com.example.fit_sentinel.ui.screens.main.reports.model.sampleDailyProgress
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun ProgressCalendar(
    dailyProgress: List<DayProgress>,
    modifier: Modifier = Modifier
) {
    MainCard(modifier = modifier) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
        ) {
            Text(
                text = "Your progress",
                modifier = Modifier.padding(16.dp),
                fontSize = 16.sp,
                style = MaterialTheme.typography.titleMedium,
            )

            HorizontalDivider(modifier = Modifier.fillMaxWidth())

            CalendarHeader()

            if (dailyProgress.isNotEmpty())
                LazyVerticalGrid(
                    columns = GridCells.Fixed(7),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                ) {
                    items(dailyProgress) { dayProgress ->
                        DayView(dayProgress = dayProgress)
                    }
                }

        }
    }
}

@Preview
@Composable
fun MyStepCounterAppScreen() {

    Fit_SentinelTheme {
        ProgressCalendar(dailyProgress = sampleDailyProgress)
    }
}