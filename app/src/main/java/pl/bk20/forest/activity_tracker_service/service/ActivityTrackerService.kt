package pl.bk20.forest.activity_tracker_service.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import pl.bk20.forest.R
import pl.bk20.forest.activity_tracker_service.service.helpers.ViewModelLifecycleService
import pl.bk20.forest.activity_tracker_service.service.helpers.viewModels


class ActivityTrackerService : ViewModelLifecycleService() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "activity_tracker_notification_channel"
        const val NOTIFICATION_ID = 0x10
    }

    private val viewModel: ActivityTrackerViewModel by viewModels { ActivityTrackerViewModel.Factory }

    private lateinit var notificationManager: NotificationManager

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        startActivityTrackerForegroundService()
    }

    private fun startActivityTrackerForegroundService() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationChannel = createDailyProgressNotificationChannel()
            registerNotificationChannel(notificationChannel)
        }
        val dailyProgressNotification = createDailyProgressNotification()
        startForeground(NOTIFICATION_ID, dailyProgressNotification)
    }

    @RequiresApi(VERSION_CODES.O)
    private fun createDailyProgressNotificationChannel(): NotificationChannel {
        val displayName = getString(R.string.activity_tracker_channel_name)
        val importance = NotificationManager.IMPORTANCE_LOW
        return NotificationChannel(NOTIFICATION_CHANNEL_ID, displayName, importance).apply {
            setShowBadge(false)
        }
    }

    @RequiresApi(VERSION_CODES.O)
    private fun registerNotificationChannel(channel: NotificationChannel) {
        notificationManager.createNotificationChannel(channel)
    }

    private fun createDailyProgressNotification(): Notification {
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.nature_fill0_wght400_grad0_opsz24)
            .setContentTitle("Forest is running (and so are you)")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSilent(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
    }
}