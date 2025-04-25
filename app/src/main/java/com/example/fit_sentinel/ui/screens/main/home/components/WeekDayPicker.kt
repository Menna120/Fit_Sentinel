package com.example.fit_sentinel.ui.screens.main.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import java.time.LocalDate
import java.time.temporal.TemporalAdjusters
import java.time.temporal.WeekFields

@Composable
fun WeekDayPicker(
    selectedDate: LocalDate,
    onDateSelected: (LocalDate) -> Unit,
    modifier: Modifier = Modifier,
    cardWidth: Dp = 72.dp,
    selectedCardColor: Color = MaterialTheme.colorScheme.primary,
    unselectedCardColor: Color = MaterialTheme.colorScheme.background,
    selectedTextColor: Color = MaterialTheme.colorScheme.onPrimary,
    unselectedTextColor: Color = Black.copy(alpha = .2f),
) {
    val weekFields = WeekFields.of(java.util.Locale.getDefault())
    val weekNumber = selectedDate.get(weekFields.weekOfWeekBasedYear())

    val dates = remember(selectedDate.dayOfWeek, weekNumber) {

        val firstDayOfWeek =
            selectedDate.with(TemporalAdjusters.previousOrSame(java.time.DayOfWeek.SATURDAY))
        List(7) { i -> firstDayOfWeek.plusDays(i.toLong()) }
    }

    val listState = rememberLazyListState()

    LaunchedEffect(selectedDate, dates) {
        val selectedIndex = dates.indexOf(selectedDate)
        if (selectedIndex != -1) listState.animateScrollToItem(selectedIndex)
        else listState.animateScrollToItem(0)
    }


    LazyRow(
        state = listState,
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(8.dp),
        contentPadding = PaddingValues(horizontal = 16.dp)
    ) {
        itemsIndexed(dates) { index, date ->
            val isSelected = date == selectedDate
            DayCard(
                date = date,
                isSelected = isSelected,
                cardWidth = cardWidth,
                selectedCardColor = selectedCardColor,
                unselectedCardColor = unselectedCardColor,
                selectedTextColor = selectedTextColor,
                unselectedTextColor = unselectedTextColor,
                onDayClick = { clickedDate ->
                    onDateSelected(clickedDate)
                }
            )
        }
    }
}


@Preview(showBackground = true)
@Composable
fun WeekDayPickerPreview() {
    var selectedDate by remember { mutableStateOf(LocalDate.now()) }
    Fit_SentinelTheme {
        WeekDayPicker(
            selectedDate = selectedDate,
            onDateSelected = { date -> selectedDate = date }
        )
    }
}
