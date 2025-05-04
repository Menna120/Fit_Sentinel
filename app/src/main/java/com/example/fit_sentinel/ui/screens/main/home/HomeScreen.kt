package com.example.fit_sentinel.ui.screens.main.home

import android.Manifest
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.provider.Settings
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionDialog
import com.google.accompanist.permissions.ExperimentalPermissionsApi
import com.google.accompanist.permissions.rememberPermissionState
import com.google.accompanist.permissions.shouldShowRationale
import kotlinx.coroutines.flow.collectLatest

@RequiresApi(Build.VERSION_CODES.Q)
@OptIn(ExperimentalPermissionsApi::class)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val activityRecognitionPermissionState =
        rememberPermissionState(Manifest.permission.ACTIVITY_RECOGNITION)
    val uiState by viewModel.uiState.collectAsState()
    val currentPermissionDialogRequest by viewModel.currentPermissionDialogRequest.collectAsState()

    LaunchedEffect(true) {
        val isGranted = ContextCompat.checkSelfPermission(
            context,
            Manifest.permission.ACTIVITY_RECOGNITION
        ) == PackageManager.PERMISSION_GRANTED
        val shouldShowRationale = activityRecognitionPermissionState.status.shouldShowRationale

        if (!isGranted && !shouldShowRationale) {
            activityRecognitionPermissionState.launchPermissionRequest()
        }
    }

    LaunchedEffect(viewModel.navigateToSettings) {
        viewModel.navigateToSettings.collect {
            openAppSettings(context)
        }
    }

    LaunchedEffect(viewModel.toastEvent) {
        viewModel.toastEvent.collectLatest { event ->
            val message = when (event) {
                HomeToastEvent.SessionStarted -> "Session Started"
                HomeToastEvent.SessionEnded -> "Session Ended"
            }
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    HomeLayout(
        selectedDate = uiState.selectedDate,
        steps = uiState.totalSteps,
        targetSteps = uiState.targetSteps,
        time = uiState.time,
        calories = uiState.calories,
        distance = uiState.distance,
        isRecording = uiState.isRecording,
        onPreviousMonth = viewModel::onPreviousWeekClicked,
        onNextMonth = viewModel::onNextWeekClicked,
        onDateSelected = viewModel::onDateSelected,
        onButtonClick = {
            viewModel.onStepProgressButtonClicked(activityRecognitionPermissionState.status)
        },
        modifier = modifier
    )

    AnimatedVisibility(visible = currentPermissionDialogRequest != null) {
        currentPermissionDialogRequest?.let { request ->
            PermissionDialog(
                request = request,
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                    viewModel.onPermissionRationaleOkClick()
                    activityRecognitionPermissionState.launchPermissionRequest()
                },
                onGoToAppSettingsClick = viewModel::onPermissionPermanentlyDeclinedSettingsClick
            )
        }
    }
}

fun openAppSettings(context: Context) {
    val intent = Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS).apply {
        data = Uri.fromParts("package", context.packageName, null)
    }
    context.startActivity(intent)
}