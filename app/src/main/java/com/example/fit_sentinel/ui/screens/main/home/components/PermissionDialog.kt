package com.example.fit_sentinel.ui.screens.main.home.components


import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier

data class PermissionDialogRequest(
    val permission: String,
    val isPermanentlyDeclined: Boolean,
    val permissionTextProvider: PermissionTextProvider
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun PermissionDialog(
    request: PermissionDialogRequest,
    onDismiss: () -> Unit,
    onOkClick: () -> Unit,
    onGoToAppSettingsClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text(text = "Permission required") },
        text = {
            Text(
                text = request.permissionTextProvider.getDescription(
                    isPermanentlyDeclined = request.isPermanentlyDeclined
                )
            )
        },
        confirmButton = {
            Button(onClick = {
                if (request.isPermanentlyDeclined) {
                    onGoToAppSettingsClick()
                } else {
                    onOkClick()
                }
            }) {
                Text(if (request.isPermanentlyDeclined) "Settings" else "Continue")
            }
        },
        dismissButton = {
            if (!request.isPermanentlyDeclined) {
                Button(onClick = onDismiss) {
                    Text("Not Now")
                }
            }
        },
        modifier = modifier
    )
}

interface PermissionTextProvider {
    fun getDescription(isPermanentlyDeclined: Boolean): String
}

class ActivityRecognitionPermissionTextProvider : PermissionTextProvider {
    override fun getDescription(isPermanentlyDeclined: Boolean): String {
        return if (isPermanentlyDeclined) {
            "It seems you permanently declined Physical Activity permission needed for step tracking. " +
                    "Please go to the app settings to grant it manually."
        } else {
            "This app needs access to your Physical Activity data to accurately record your steps, distance, and calories burned."
        }
    }
}