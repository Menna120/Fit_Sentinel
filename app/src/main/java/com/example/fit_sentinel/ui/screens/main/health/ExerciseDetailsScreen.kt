package com.example.fit_sentinel.ui.screens.main.health

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.fit_sentinel.R
import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.ui.common.MainButton
import com.example.fit_sentinel.ui.screens.main.health.components.ExerciseDetailsRow
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme
import kotlinx.coroutines.launch

@Composable
fun ExerciseDetailsLayout(
    index: Int,
    exercisesList: List<Exercise>,
    modifier: Modifier = Modifier,
    onBackClick: () -> Unit
) {
    val pagerState = rememberPagerState(initialPage = index) { exercisesList.size }
    val scope = rememberCoroutineScope()
    val enablePrevious = remember(pagerState.currentPage) { pagerState.currentPage > 0 }
    val enableNext =
        remember(pagerState.currentPage) { pagerState.currentPage < exercisesList.size - 1 }

    Box(modifier = modifier.fillMaxSize(), contentAlignment = Alignment.TopStart) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(bottom = 16.dp),
            verticalArrangement = Arrangement.SpaceBetween,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Image(
                painter = painterResource(R.drawable.exercise),
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1f)
            )
            HorizontalPager(
                state = pagerState,
                modifier = Modifier
                    .weight(1f)
                    .fillMaxWidth(.8f),
                userScrollEnabled = false
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    verticalArrangement = Arrangement.spacedBy(32.dp)
                ) {
                    Text(
                        exercisesList[page].exerciseName,
                        style = MaterialTheme.typography.headlineMedium,
                        color = MaterialTheme.colorScheme.primary
                    )
                    Text(
                        exercisesList[page].description,
                        style = MaterialTheme.typography.titleMedium,
                        color = MaterialTheme.colorScheme.onBackground.copy(alpha = .4f)
                    )
                    ExerciseDetailsRow(exercisesList[page].reps, exercisesList[page].sets)
                }
            }
            Row(modifier = Modifier.fillMaxWidth(.8f)) {
                MainButton(
                    "Previous",
                    enablePrevious,
                    Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = MaterialTheme.colorScheme.background,
                        contentColor = MaterialTheme.colorScheme.primary,
                        disabledContainerColor = MaterialTheme.colorScheme.background,
                        disabledContentColor = MaterialTheme.colorScheme.primary.copy(alpha = .4f)
                    ),
                    border = ButtonDefaults.outlinedButtonBorder(enablePrevious).copy(
                        brush = SolidColor(
                            MaterialTheme.colorScheme.primary
                        )
                    ),
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage - 1
                        )
                    }
                }

                Spacer(modifier = Modifier.weight(.2f))

                MainButton(
                    "Next",
                    enableNext,
                    Modifier.weight(1f),
                    textStyle = MaterialTheme.typography.titleMedium.copy(fontSize = 20.sp)
                ) {
                    scope.launch {
                        pagerState.animateScrollToPage(
                            pagerState.currentPage + 1
                        )
                    }
                }
            }
        }

        IconButton(onBackClick, modifier = Modifier.padding(24.dp)) {
            Icon(
                painter = painterResource(R.drawable.back),
                contentDescription = null
            )
        }
    }

}

@Preview(showBackground = true)
@Composable
private fun ExerciseDetailsLayoutPreview() {
    Fit_SentinelTheme {
        ExerciseDetailsLayout(
            index = 2,
            exercisesList = listOf(
                Exercise(
                    exerciseName = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
                Exercise(
                    exerciseName = "Barbell Squats",
                    sets = 5,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
                Exercise(
                    exerciseName = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
                Exercise(
                    exerciseName = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
            )
        ) {}
    }
}
