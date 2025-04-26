package com.example.fit_sentinel.ui.screens.forms

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material3.Icon
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ProgressIndicatorDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun OnboardingContent() {

    var name by remember { mutableStateOf("") }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.SpaceBetween,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Header Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back),
                contentDescription = "Back",
                tint = Color.Black
            )
            LinearProgressIndicator(
                progress = { 1f / 9f },
                modifier = Modifier.weight(1f),
                color = MaterialTheme.colorScheme.primary,
                //                backgroundColor = lightPurpleColor,
                trackColor = MaterialTheme.colorScheme.primary.copy(alpha = .2f),
                strokeCap = ProgressIndicatorDefaults.LinearStrokeCap,
            )
            Text(
                text = "1 / 9",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 16.sp
                )
            )
        }

        // Main Content Area
        Column(
            modifier = Modifier.fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(
                text = "Personalized Program",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = "Before starting, please answer a few simple questions.",
                style = TextStyle(
                    color = Color.Gray,
                    fontSize = 16.sp
                ),
                textAlign = TextAlign.Center
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = "What is your name?",
                style = TextStyle(
                    color = Color.Black,
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold
                )
            )
            Spacer(modifier = Modifier.height(16.dp))
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(50.dp)
                    .shadow(elevation = 4.dp, shape = RoundedCornerShape(8.dp))
                    .background(Color.White, RoundedCornerShape(8.dp))
                    .border(
                        width = 1.dp,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f),
                        shape = RoundedCornerShape(8.dp)
                    )
                    .padding(start = 16.dp)
            ) {
                if (name.isEmpty()) {
                    Text(
                        text = "Name",
                        style = TextStyle(
                            color = MaterialTheme.colorScheme.onBackground.copy(alpha = .3f),
                            fontSize = 16.sp
                        )
                    )
                }
                BasicTextField(
                    value = name,
                    onValueChange = { name = it },
                    textStyle = TextStyle(
                        color = if (name.isEmpty()) MaterialTheme.colorScheme.onBackground.copy(
                            alpha = .3f
                        ) else Color.Black,
                        fontSize = 16.sp
                    ),
                    modifier = Modifier.padding(start = 32.dp)
                )
                Icon(
                    painter = painterResource(id = R.drawable.name),
                    contentDescription = "Person",
                    tint = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.align(Alignment.CenterStart)
                )
            }
        }
        MainButton(
            onClick = { /* Handle Continue Click */ },
            text = "Continue",
            showIcon = false,
            enabled = false
        )

        // Footer Section
//        Button(
//            onClick = { /* Handle Continue Click */ },
//            modifier = Modifier
//                .fillMaxWidth()
//                .height(50.dp),
//            shape = RoundedCornerShape(8.dp),
//            colors = ButtonDefaults.buttonColors(
//                containerColor = MaterialTheme.colorScheme.primary.copy(
//                    alpha = .2f
//                )
//            )
//        ) {
//            Text(
//                text = "Continue",
//                style = TextStyle(
//                    color = Color.White,
//                    fontSize = 16.sp
//                )
//            )
//        }
    }
}

@Preview(showBackground = true)
@Composable
private fun OnBoardingPreview() {
    Fit_SentinelTheme {
        OnboardingContent()
    }
}