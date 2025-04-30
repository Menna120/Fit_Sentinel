package com.example.fit_sentinel.domain.model

import androidx.compose.ui.graphics.Color
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Blue
import com.example.fit_sentinel.ui.theme.Pink

enum class Gender(val color: Color) {
    Female(Pink),
    Male(Blue),
    PreferNotToSay(Black)
}