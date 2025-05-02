package com.example.fit_sentinel.ui.screens.main.home

import android.Manifest
import android.app.Activity
import android.content.pm.PackageManager
import android.os.Build
import androidx.activity.compose.LocalActivity
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.annotation.RequiresApi
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionDialog
import com.example.fit_sentinel.ui.screens.main.home.components.PermissionState

@RequiresApi(Build.VERSION_CODES.Q)
@Composable
fun HomeScreen(
    modifier: Modifier = Modifier,
    viewModel: HomeViewModel = hiltViewModel()
) {
    val activity = LocalActivity.current as Activity
    val permissionsToRequest = Manifest.permission.ACTIVITY_RECOGNITION

    val uiState by viewModel.uiState.collectAsState()
    val dialogQueue = viewModel.visiblePermissionDialogQueue.toList()
    val permissionState by viewModel.activityRecognitionPermissionState.collectAsState()

    val activityRecognitionPermissionResultLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.RequestPermission(),
        onResult = { isGranted ->
            val shouldShowRationale = ActivityCompat.shouldShowRequestPermissionRationale(
                activity,
                permissionsToRequest
            )
            viewModel.onPermissionStatusDetermined(
                permission = permissionsToRequest,
                isGranted = isGranted,
                shouldShowRationale = shouldShowRationale
            )
        }
    )

    LaunchedEffect(Unit) {
        val isGranted = ContextCompat.checkSelfPermission(
            activity,
            permissionsToRequest
        ) == PackageManager.PERMISSION_GRANTED
        val shouldShowRationale =
            ActivityCompat.shouldShowRequestPermissionRationale(activity, permissionsToRequest)

        viewModel.onPermissionStatusDetermined(
            permission = permissionsToRequest,
            isGranted = isGranted,
            shouldShowRationale = shouldShowRationale
        )
        if (!isGranted && !shouldShowRationale) {
            activityRecognitionPermissionResultLauncher.launch(permissionsToRequest)
        }
    }

    LaunchedEffect(viewModel.requestPermissionEvent) {
        viewModel.requestPermissionEvent.collect { permission ->
            activityRecognitionPermissionResultLauncher.launch(permission)
        }
    }

    LaunchedEffect(viewModel.navigateToSettings) {
        viewModel.navigateToSettings.collect {
            activity.openAppSettings()
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
            viewModel.onStepProgressButtonClicked(activity, permissionsToRequest)
        },
        modifier = modifier
    )
    AnimatedVisibility(permissionState == PermissionState.DENIED_RATIONALE || permissionState == PermissionState.DENIED_PERMANENT) {
        dialogQueue.forEach { request ->
            PermissionDialog(
                request = request,
                onDismiss = viewModel::dismissDialog,
                onOkClick = {
                    viewModel.onPermissionRationaleOkClick(request.permission)
                },
                onGoToAppSettingsClick = {
                    viewModel.onPermissionPermanentlyDeclinedSettingsClick()
                }
            )
        }
    }
}
