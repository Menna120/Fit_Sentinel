package com.example.fit_sentinel.ui.screens.main.home.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.ui.common.Arc
import com.example.fit_sentinel.ui.theme.Black
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun StepProgressDisplay(
    steps: Int,
    targetSteps: Int,
    onButtonClick: () -> Unit,
    modifier: Modifier = Modifier,
    labelText: String = "Steps",
    buttonIcon: ImageVector = Icons.Default.PlayArrow,
    labelColor: Color = Black.copy(alpha = .5f),
    stepsColor: Color = Black,
    buttonBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    buttonIconColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    Arc(steps, targetSteps, modifier) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.spacedBy(24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceBetween,
                modifier = Modifier
                    .weight(1f)
                    .padding(top = 82.dp)
            ) {
                Text(
                    text = labelText,
                    color = labelColor,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

                Text(
                    text = steps.toString(),
                    color = stepsColor,
                    style = MaterialTheme.typography.displayLarge
                )

                Text(
                    text = targetSteps.toString(),
                    color = labelColor,
                    modifier = Modifier.padding(bottom = 8.dp),
                    style = MaterialTheme.typography.titleMedium
                )

            }

            Button(
                onClick = onButtonClick,
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor),
                modifier = Modifier
                    .padding(bottom = 16.dp)
                    .size(56.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                Icon(
                    imageVector = buttonIcon,
                    contentDescription = "Start/Stop",
                    tint = buttonIconColor,
                    modifier = Modifier.size(32.dp)
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewStepProgressDisplay() {
    Fit_SentinelTheme {
        StepProgressDisplay(
            steps = 0,
            targetSteps = 6000,
            onButtonClick = {}
        )
    }
}
