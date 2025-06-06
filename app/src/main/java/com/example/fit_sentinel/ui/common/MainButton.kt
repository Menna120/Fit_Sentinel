package com.example.fit_sentinel.ui.common

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonColors
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Shape
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.tooling.preview.Preview
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun MainButton(
    text: String,
    enabled: Boolean,
    modifier: Modifier = Modifier,
    showIcon: Boolean = false,
    shape: Shape = CircleShape,
    colors: ButtonColors = ButtonDefaults.buttonColors(
        disabledContainerColor = MaterialTheme.colorScheme.secondary,
        disabledContentColor = MaterialTheme.colorScheme.onPrimary
    ),
    border: BorderStroke? = null,
    textStyle: TextStyle = MaterialTheme.typography.headlineMedium,
    onClick: () -> Unit
) {
    Button(
        onClick = onClick,
        modifier = modifier.fillMaxWidth(),
        enabled = enabled,
        shape = shape,
        border = border,
        colors = colors
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.Center,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(text, style = textStyle)
            if (showIcon) {
                Spacer(modifier = Modifier.weight(1f))
                Icon(
                    painterResource(R.drawable.arrow_up_line),
                    "let's go",
                    tint = Color.Unspecified
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun MainButtonPreview() {
    Fit_SentinelTheme {
        MainButton("Let’s get started", true) {}
    }
}