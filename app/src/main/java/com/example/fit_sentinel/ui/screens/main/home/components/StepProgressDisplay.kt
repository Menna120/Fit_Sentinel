package com.example.fit_sentinel.ui.screens.main.home.components

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.R
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
    labelColor: Color = Black.copy(alpha = .5f),
    stepsColor: Color = Black,
    buttonBackgroundColor: Color = MaterialTheme.colorScheme.primary,
    buttonIconColor: Color = MaterialTheme.colorScheme.onPrimary
) {
    var buttonState by remember { mutableStateOf(false) }

    Arc(steps, targetSteps, modifier) {
        Column(
            modifier = Modifier.fillMaxHeight(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = labelText,
                    color = labelColor,
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
                    style = MaterialTheme.typography.titleMedium
                )
            }

            Button(
                onClick = {
                    buttonState = !buttonState
                    onButtonClick()
                },
                colors = ButtonDefaults.buttonColors(containerColor = buttonBackgroundColor),
                modifier = Modifier.size(50.dp),
                shape = CircleShape,
                contentPadding = PaddingValues(0.dp)
            ) {
                AnimatedContent(targetState = buttonState, label = "") { isPlaying ->
                    Icon(
                        painter = painterResource(if (isPlaying) R.drawable.equal else R.drawable.play),
                        contentDescription = "Start/Stop",
                        tint = buttonIconColor,
                        modifier = Modifier
                            .size(24.dp)
                    )
                }

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
