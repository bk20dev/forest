package pl.bk20.forest.activity_tracker_service.service

import android.app.Notification
import androidx.core.app.NotificationCompat
import pl.bk20.forest.R
import pl.bk20.forest.activity_tracker_service.service.helpers.ViewModelLifecycleService
import pl.bk20.forest.activity_tracker_service.service.helpers.viewModels


class ActivityTrackerService : ViewModelLifecycleService() {

    private val viewModel: ActivityTrackerViewModel by viewModels { ActivityTrackerViewModel.Factory }

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "step_counter_channel"
        const val NOTIFICATION_ID = 0x1
    }

    override fun onCreate() {
        super.onCreate()

        val dailyProgressNotification = createDailyProgressNotification()
        startForeground(NOTIFICATION_ID, dailyProgressNotification)
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