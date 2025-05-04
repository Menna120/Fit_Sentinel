package com.example.fit_sentinel.ui.screens.on_boarding.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldColors
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IconTextInputField(
    value: String,
    onValueChange: (String) -> Unit,
    placeholderText: String,
    modifier: Modifier = Modifier,
    colors: TextFieldColors = TextFieldDefaults.colors(
        focusedContainerColor = MaterialTheme.colorScheme.background,
        unfocusedContainerColor = MaterialTheme.colorScheme.background,
        disabledContainerColor = MaterialTheme.colorScheme.background,
        focusedIndicatorColor = Color.Transparent,
        unfocusedIndicatorColor = Color.Transparent,
        disabledIndicatorColor = Color.Transparent,
        focusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        unfocusedLeadingIconColor = MaterialTheme.colorScheme.primary,
        focusedPlaceholderColor = MaterialTheme.colorScheme.onBackground,
        unfocusedPlaceholderColor = MaterialTheme.colorScheme.onBackground.copy(alpha = 0.3f),
    ),
    shape: RoundedCornerShape = RoundedCornerShape(8.dp),
    shadowElevation: Dp = 4.dp
) {
    TextField(
        value = value,
        onValueChange = onValueChange,
        modifier = modifier
            .fillMaxWidth()
            .shadow(elevation = shadowElevation, shape = shape)
            .then(Modifier.background(MaterialTheme.colorScheme.background, shape)),
        textStyle = MaterialTheme.typography.titleLarge,
        leadingIcon = {
            Icon(
                painter = painterResource(id = R.drawable.name),
                contentDescription = null
            )
        },
        placeholder = {
            Text(placeholderText, style = MaterialTheme.typography.titleLarge)
        },
        colors = colors,
        shape = shape,
        singleLine = true
    )
}

@Preview
@Composable
private fun IconTextInputFieldPreview() {
    var name by remember { mutableStateOf("") }
    Fit_SentinelTheme {
        IconTextInputField(name, { name = it }, "name")
    }
}