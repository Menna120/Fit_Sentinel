package com.example.fit_sentinel.ui.screens.main.home.components

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import java.time.LocalDate
import java.time.format.TextStyle
import java.util.Locale

@Composable
fun DayCard(
    date: LocalDate,
    isSelected: Boolean,
    cardWidth: Dp,
    selectedCardColor: Color,
    unselectedCardColor: Color,
    selectedTextColor: Color,
    unselectedTextColor: Color,
    onDayClick: (LocalDate) -> Unit
) {
    val cardColor = if (isSelected) selectedCardColor else unselectedCardColor
    val textColor = if (isSelected) selectedTextColor else unselectedTextColor

    Card(
        modifier = Modifier
            .width(cardWidth)
            .aspectRatio(.8f)
            .clickable { onDayClick(date) },
        shape = RoundedCornerShape(8.dp),
        border = if (!isSelected) BorderStroke(1.dp, unselectedTextColor) else null,
        colors = CardDefaults.cardColors(containerColor = cardColor)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceAround
        ) {
            Text(
                text = date.month.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .uppercase(Locale.getDefault()),
                color = textColor,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall
            )
            Text(
                text = date.dayOfMonth.toString(),
                color = textColor,
                fontSize = 24.sp,
                style = MaterialTheme.typography.headlineSmall
            )
            Text(
                text = date.dayOfWeek.getDisplayName(TextStyle.SHORT, Locale.getDefault())
                    .uppercase(Locale.getDefault()),
                color = textColor,
                fontSize = 12.sp,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}

@Preview
@Composable
fun PreviewDayCardSelected() {
    Fit_SentinelTheme {
        DayCard(
            date = LocalDate.now(),
            isSelected = true,
            cardWidth = 72.dp,
            selectedCardColor = MaterialTheme.colorScheme.primary,
            unselectedCardColor = MaterialTheme.colorScheme.background,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = Black.copy(alpha = .2f),
            onDayClick = {}
        )
    }
}

@Preview
@Composable
fun PreviewDayCardUnselected() {
    Fit_SentinelTheme {
        DayCard(
            date = LocalDate.now().plusDays(1),
            isSelected = false,
            cardWidth = 72.dp,
            selectedCardColor = MaterialTheme.colorScheme.primary,
            unselectedCardColor = MaterialTheme.colorScheme.background,
            selectedTextColor = MaterialTheme.colorScheme.onPrimary,
            unselectedTextColor = Black.copy(alpha = .2f),
            onDayClick = {}
        )
    }
}