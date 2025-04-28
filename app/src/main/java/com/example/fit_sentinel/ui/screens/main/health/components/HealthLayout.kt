package com.example.fit_sentinel.ui.screens.main.health.components

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.fit_sentinel.data.remote.dto.Exercise
import com.example.fit_sentinel.ui.screens.main.health.components.bmi_scalar.ScaleIndicatorBar
import com.example.fit_sentinel.ui.theme.Fit_SentinelTheme

@Composable
fun HealthLayout(
    value: Float,
    categoryLabel: String,
    exercises: List<Exercise>,
    modifier: Modifier = Modifier,
    onExerciseClick: (index: Int) -> Unit,
) {
    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(24.dp)
    ) {
        ScaleIndicatorBar(value, categoryLabel)

        Text("Recommendation Trainings", style = MaterialTheme.typography.titleMedium)

        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(24.dp)
        ) {
            itemsIndexed(exercises) { index, exercise ->
                ExerciseCard(
                    exerciseName = exercise.exercise_name,
                    sets = exercise.sets,
                    reps = exercise.reps,
                    modifier = Modifier.clickable { onExerciseClick(index) },
                )
            }
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun HealthLayoutPreview() {
    Fit_SentinelTheme {
        HealthLayout(
            value = 24.22f,
            categoryLabel = "Healthy weight",
            exercises = listOf(
                Exercise(
                    exercise_name = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
                Exercise(
                    exercise_name = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
                Exercise(
                    exercise_name = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
                Exercise(
                    exercise_name = "Barbell Squats",
                    sets = 3,
                    reps = 12,
                    description = "Full range of motion, focus on form. Use a weight that challenges you while maintaining good technique."
                ),
            ),
            onExerciseClick = {}
        )
    }
}