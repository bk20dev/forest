package pl.bk20.forest.service

import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.Service
import android.content.Intent
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.IBinder
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import pl.bk20.forest.R

class StepCounterService : Service() {

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 0x1
    }

    override fun onCreate() {
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationChannel = createNotificationChannel()
            registerNotificationChannel(notificationChannel)
        }
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val notification = NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)

        startForeground(NOTIFICATION_ID, notification.build())

        return START_STICKY
    }

    override fun onBind(intent: Intent?): IBinder? = null

    @RequiresApi(VERSION_CODES.O)
    private fun createNotificationChannel(): NotificationChannel {
        val name = getString(R.string.step_counter_channel)
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        return NotificationChannel(NOTIFICATION_CHANNEL_ID, name, importance)
    }

    @RequiresApi(VERSION_CODES.O)
    private fun registerNotificationChannel(channel: NotificationChannel) {
        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }
}