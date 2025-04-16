package com.example.fit_sentinel.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import android.os.Build
import android.util.Log
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fit_sentinel.data.model.SensorMode
import com.example.fit_sentinel.ui.viewmodel.StepViewModel

@Composable
fun StepCounterScreen(
    viewModel: StepViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val context = LocalContext.current

    val snackBarHostState = remember { SnackbarHostState() }

    // --- Permission Handling ---
    var hasPermission by remember { mutableStateOf(false) }

    val permissionLauncher = rememberLauncherForActivityResult(
        ActivityResultContracts.RequestPermission()
    ) { isGranted: Boolean ->
        hasPermission = isGranted
        if (isGranted) {
            Log.d("Permissions", "ACTIVITY_RECOGNITION granted.")
            // Optional: You might want to automatically start tracking if permission
            // is granted here and it wasn't already granted.
            // Be careful not to start it multiple times if already running.
            if (!uiState.isTracking) {
//                 viewModel.startTracking() // Decide if auto-start is desired
            }
        } else {
            Log.w("Permissions", "ACTIVITY_RECOGNITION denied.")
            // Consider showing a message explaining why the permission is needed
            // using the snackbarHostState or a Text element.
            // viewModel.updateErrorMessage("Step tracking requires Activity Recognition permission.")
        }
    }

    // Check and request permission
    LaunchedEffect(Unit) { // Use Unit to run only once on composition
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.Q) {
            when (ContextCompat.checkSelfPermission(context, Manifest.permission.ACTIVITY_RECOGNITION)) {
                PackageManager.PERMISSION_GRANTED -> {
                    Log.d("Permissions", "ACTIVITY_RECOGNITION already granted.")
                    hasPermission = true
                }
                else -> {
                    Log.d("Permissions", "Requesting ACTIVITY_RECOGNITION permission.")
                    permissionLauncher.launch(Manifest.permission.ACTIVITY_RECOGNITION)
                }
            }
        } else {
            // On older devices, permission is usually granted at install time (though check specifics)
            Log.d("Permissions", "Running on pre-Q device, permission assumed.")
            hasPermission = true
        }
    }
    // --- End Permission Handling ---


    Scaffold(
        // Provide the explicit SnackbarHostState to the Scaffold
        snackbarHost = { SnackbarHost(hostState = snackBarHostState) }
    ) { paddingValues ->

        // --- Handle Error Message Display via Snackbar ---
        // LaunchedEffect will run whenever uiState.errorMessage changes FROM null TO non-null
        // or from one error message to another.
        val errorMessage = uiState.errorMessage
        LaunchedEffect(errorMessage) {
            errorMessage?.let { msg -> // Check if message is non-null inside the effect
                Log.d("Snackbar", "Showing snackbar for error: $msg")
                snackBarHostState.showSnackbar(
                    message = msg,
                    duration = SnackbarDuration.Short
                )
                // Crucially, clear the error state in the ViewModel AFTER the snackbar
                // has been shown (or the effect is cancelled).
                viewModel.clearError()
                Log.d("Snackbar", "Error message cleared after snackbar.")
            }
        }
        // --- End Error Handling ---


        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {

            if (!uiState.isSensorAvailable) {
                Text("Step Counter Sensor Not Available", color = MaterialTheme.colorScheme.error)
                Spacer(modifier = Modifier.height(20.dp))
            } else if (uiState.sensorMode == SensorMode.ACCELEROMETER) {
                // Optionally show a warning for accelerometer mode
                Text(
                    "Using Accelerometer Mode (Higher Battery Usage)",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            Text("Steps Today", style = MaterialTheme.typography.headlineMedium)
            Spacer(modifier = Modifier.height(8.dp))

            Text(
                text = "${uiState.totalStepsToday}",
                fontSize = 48.sp,
                fontWeight = androidx.compose.ui.text.font.FontWeight.Bold
            )
            Text(
                text = "(Session: ${uiState.currentSessionSteps})",
                fontSize = 16.sp,
                color = MaterialTheme.colorScheme.outline
            )

            Spacer(modifier = Modifier.height(32.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                Button(
                    onClick = { viewModel.startTracking() },
                    // Enable only if permission granted, sensor available, AND not already tracking
                    enabled = hasPermission && uiState.isSensorAvailable && !uiState.isTracking
                ) {
                    Text("Start Tracking")
                }
                Button(
                    onClick = { viewModel.stopTracking() },
                    enabled = uiState.isTracking // Enable only if currently tracking
                ) {
                    Text("Stop Tracking")
                }
            }

            Spacer(modifier = Modifier.height(32.dp))

            Button(
                onClick = { /*viewModel.requestAnalysis()*/ },
                enabled = !uiState.isLoadingAnalysis && uiState.totalStepsToday > 0
            ) {
                if (uiState.isLoadingAnalysis) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp), strokeWidth = 2.dp)
                } else {
                    Text("Analyze Steps")
                }
            }

            uiState.lastAnalysis?.let { analysis ->
                Spacer(modifier = Modifier.height(24.dp))
                Card(modifier = Modifier.fillMaxWidth()) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text("Analysis Results", style = MaterialTheme.typography.titleMedium)
                        Spacer(modifier = Modifier.height(8.dp))
                        Text("Distance: ${String.format("%.2f", analysis.estimatedDistanceKm)} km")
                        Text("Calories Burned: ${analysis.estimatedCaloriesBurned} kcal")
                        Text("Suggested Steps: ${analysis.suggestedStepsToday}")
                        analysis.message?.let { Text("AI Message: $it") }
                    }
                }
            }

            // Button to test saving (optional)
            Spacer(modifier = Modifier.height(16.dp))
            Button(onClick = { viewModel.saveCurrentTotalSteps() }) {
                Text("Save Daily Total (Test)")
            }
        }
    }
}