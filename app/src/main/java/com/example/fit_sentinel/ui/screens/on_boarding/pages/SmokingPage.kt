package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.annotation.DrawableRes
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.RadioButton
import androidx.compose.material3.RadioButtonDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer
import com.example.fit_sentinel.ui.screens.on_boarding.model.SmokingOption
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme


@Composable
fun SmokingPage(
    selectedSmokingOption: SmokingOption?,
    modifier: Modifier = Modifier,
    onSmokingOptionClick: (Any) -> Unit
) {
    QuestionContainer("Are you a smoker ?", modifier = modifier.fillMaxSize()) {
        SmokeRow(
            selectedSmokingOption = selectedSmokingOption,
            onOptionClick = onSmokingOptionClick
        )
    }
}

@Composable
fun SmokeRow(
    selectedSmokingOption: SmokingOption?,
    modifier: Modifier = Modifier,
    onOptionClick: (Any) -> Unit
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        SelectionCard(
            option = SmokingOption.Smoking,
            icon = R.drawable.smoker,
            isSelected = selectedSmokingOption == SmokingOption.Smoking,
            onCardClick = onOptionClick,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
        Spacer(Modifier.width(16.dp))
        SelectionCard(
            option = SmokingOption.NoSmoking,
            icon = R.drawable.not_smoker,
            isSelected = selectedSmokingOption == SmokingOption.NoSmoking,
            onCardClick = onOptionClick,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
    }
}

@Composable
fun SelectionCard(
    option: Any,
    @DrawableRes icon: Int,
    isSelected: Boolean,
    onCardClick: (Any) -> Unit,
    modifier: Modifier = Modifier,
) {
    MainCard(modifier = modifier) {
        Box(modifier = Modifier.fillMaxSize()) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier
                    .fillMaxSize()
                    .align(Alignment.Center)
            )
            RadioButton(
                selected = isSelected,
                onClick = { onCardClick(option) },
                modifier = Modifier.align(Alignment.TopEnd),
                colors = RadioButtonDefaults.colors(
                    selectedColor = MaterialTheme.colorScheme.primary,
                    unselectedColor = MaterialTheme.colorScheme.primary
                )
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun SmokingPagePreview() {
    var selectedSmokingOption by remember { mutableStateOf<SmokingOption?>(null) }

    Fit_SentinelTheme {
        SmokingPage(
            selectedSmokingOption = selectedSmokingOption,
            onSmokingOptionClick = { selectedSmokingOption = it as SmokingOption? })
    }
}