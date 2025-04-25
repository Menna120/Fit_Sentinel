package com.example.fit_sentinel.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService // Use LifecycleService for easier scope management
import androidx.lifecycle.lifecycleScope
import com.example.fit_sentinel.MainActivity
import com.example.fit_sentinel.R
import com.example.fit_sentinel.domain.repository.StepRepository
import com.example.fit_sentinel.domain.repository.StepSensorManager
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import javax.inject.Inject

@AndroidEntryPoint
class StepCounterService : LifecycleService() { // Use LifecycleService

    @Inject
    lateinit var stepSensorManager: StepSensorManager // Inject sensor manager

    @Inject
    lateinit var repository: StepRepository // Inject repo if needed for saving

    private var isServiceRunning = false

    companion object {
        const val NOTIFICATION_ID = 101
        const val NOTIFICATION_CHANNEL_ID = "StepCounterChannel"
        const val ACTION_START_SERVICE = "ACTION_START_SERVICE"
        const val ACTION_STOP_SERVICE = "ACTION_STOP_SERVICE"
    }

    override fun onCreate() {
        super.onCreate()
        createNotificationChannel()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        super.onStartCommand(intent, flags, startId) // Important for LifecycleService

        intent?.action?.let { action ->
            when (action) {
                ACTION_START_SERVICE -> {
                    if (!isServiceRunning) {
                        startForegroundService()
                        isServiceRunning = true
                    }
                }

                ACTION_STOP_SERVICE -> {
                    stopForegroundService()
                    isServiceRunning = false
                }
            }
        }
        // START_STICKY: If the service is killed, restart it (may lose state)
        // START_NOT_STICKY: If killed, don't restart automatically
        // START_REDELIVER_INTENT: If killed, restart and redeliver the last intent
        return START_STICKY
    }

    private fun startForegroundService() {
        val notification = createNotification(0) // Initial steps count = 0
        startForeground(NOTIFICATION_ID, notification)

        // Start listening to sensor within the service's lifecycle scope
        lifecycleScope.launch {
            stepSensorManager.startListening() // Start sensor
            // Observe steps and update notification
            stepSensorManager.currentSteps.collectLatest { steps ->
                // Optional: Persist steps periodically from the service
                // repository.saveSteps(...) // Be mindful of DB write frequency
                updateNotification(steps)
            }
        }
    }

    private fun stopForegroundService() {
        stepSensorManager.stopListening() // Stop sensor
        stopForeground(STOP_FOREGROUND_REMOVE) // Remove notification
        stopSelf() // Stop the service itself
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                NOTIFICATION_CHANNEL_ID,
                "Step Counter",
                NotificationManager.IMPORTANCE_LOW // Low importance for less intrusiveness
            )
            val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
            manager.createNotificationChannel(channel)
        }
    }

    private fun createNotification(steps: Int): Notification {
        val notificationIntent = Intent(this, MainActivity::class.java) // Open app on click
        val pendingIntentFlags =
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        val pendingIntent =
            PendingIntent.getActivity(this, 0, notificationIntent, pendingIntentFlags)

        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setContentTitle("Step Counter Active")
            .setContentText("Steps today (session): $steps")
            .setSmallIcon(R.drawable.ic_launcher_foreground) // Replace with your icon
            .setContentIntent(pendingIntent)
            .setOngoing(true) // Makes it non-dismissible by swiping
            .setOnlyAlertOnce(true) // Don't vibrate/sound for updates
            .build()
    }

    private fun updateNotification(steps: Int) {
        val notification = createNotification(steps)
        val manager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        manager.notify(NOTIFICATION_ID, notification)
    }

    override fun onDestroy() {
        // Clean up resources if necessary, though stopListening should handle sensor
        super.onDestroy()
    }
}