package com.example.fit_sentinel.ui.screens.on_boarding.pages

import androidx.annotation.DrawableRes
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.domain.model.Gender
import com.example.fit_sentinel.ui.common.MainCard
import com.example.fit_sentinel.ui.screens.on_boarding.components.QuestionContainer

@Composable
fun GenderPage(
    selectedGender: Gender?,
    modifier: Modifier = Modifier,
    onGenderSelected: (Gender) -> Unit
) {
    QuestionContainer("What is your gender?", modifier = modifier.fillMaxSize()) {
        GenderRow(selectedGender = selectedGender, onGenderSelected = onGenderSelected)
    }
}

@Composable
fun GenderRow(
    selectedGender: Gender?,
    modifier: Modifier = Modifier,
    onGenderSelected: (Gender) -> Unit
) {
    Row(
        modifier = modifier.fillMaxSize(),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        GenderCard(
            gender = Gender.Male,
            icon = R.drawable.male,
            text = "Male",
            isSelected = selectedGender == Gender.Male,
            onCardClick = onGenderSelected,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
        Spacer(Modifier.width(16.dp))
        GenderCard(
            gender = Gender.Female,
            icon = R.drawable.female,
            text = "Female",
            isSelected = selectedGender == Gender.Female,
            onCardClick = onGenderSelected,
            modifier = Modifier
                .weight(1f)
                .aspectRatio(1f)
        )
    }
}

@Composable
fun GenderCard(
    gender: Gender,
    @DrawableRes icon: Int,
    text: String,
    isSelected: Boolean,
    onCardClick: (Gender) -> Unit,
    modifier: Modifier = Modifier,
    defaultContentColor: Color = MaterialTheme.colorScheme.onBackground
) {
    val cornerShape = RoundedCornerShape(8.dp)

    val borderWidth by animateDpAsState(if (isSelected) 1.dp else 0.dp, label = "borderWidth")
    val size by animateDpAsState(if (isSelected) 84.dp else 80.dp, label = "size")

    val tintColor by animateColorAsState(
        if (isSelected) gender.color else defaultContentColor,
        label = "tintColor"
    )

    val borderColor by animateColorAsState(
        if (isSelected) gender.color else Color.Transparent,
        label = "borderColor"
    )

    val cardPadding by animateDpAsState(if (isSelected) 8.dp else 16.dp, label = "cardPadding")


    MainCard(
        modifier = modifier
            .padding(cardPadding)
            .clickable { onCardClick(gender) },
        shape = cornerShape,
        border = BorderStroke(
            borderWidth,
            borderColor
        )
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = icon),
                contentDescription = text,
                tint = tintColor,
                modifier = Modifier.size(size)
            )
            Text(
                text = text,
                style = MaterialTheme.typography.titleLarge,
                color = tintColor,
                textAlign = TextAlign.Center
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
fun GenderPagePreview() {
    var selectedGender by remember { mutableStateOf<Gender?>(null) }

    GenderPage(selectedGender) { selectedGender = it }
}