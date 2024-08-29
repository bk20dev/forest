package pl.bk20.forest.activity_tracker_service.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.content.Context
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Build.VERSION_CODES
import android.os.SystemClock
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import pl.bk20.forest.R
import pl.bk20.forest.activity_tracker_service.service.helpers.ViewModelLifecycleService
import pl.bk20.forest.activity_tracker_service.service.helpers.viewModels
import java.time.Instant
import java.time.LocalDate
import java.time.ZoneId


class ActivityTrackerService : ViewModelLifecycleService() {

    companion object {
        const val NOTIFICATION_CHANNEL_ID = "activity_tracker_notification_channel"
        const val NOTIFICATION_ID = 0x10
    }

    private val viewModel: ActivityTrackerViewModel by viewModels { ActivityTrackerViewModel.Factory }

    private lateinit var notificationManager: NotificationManager
    private lateinit var sensorManager: SensorManager

    override fun onCreate() {
        super.onCreate()

        notificationManager = getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager

        startActivityTrackerForegroundService()
        registerActivityTrackerEventListener()
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
        // @formatter:off
        return NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setSmallIcon(R.drawable.nature_fill0_wght400_grad0_opsz24)
            .setContentTitle("Forest is running (and so are you)")
            .setVisibility(NotificationCompat.VISIBILITY_PUBLIC)
            .setSilent(true)
            .setOngoing(true)
            .setOnlyAlertOnce(true)
            .build()
        // @formatter:on
    }

    private fun registerActivityTrackerEventListener() {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)?.let { sensor ->
            sensorManager.registerListener(
                stepCounterEventListener, sensor, SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    override fun onDestroy() {
        stopActivityTrackerEventListener()
        super.onDestroy()
    }

    private fun stopActivityTrackerEventListener() {
        sensorManager.unregisterListener(stepCounterEventListener)
    }

    private val stepCounterEventListener = object : SensorEventListener {
        private var previousStepCount: Int? = null

        override fun onSensorChanged(event: SensorEvent?) {
            event?.run {
                val eventStepCount = values[0].toInt()
                val timestamp = eventTimestampNanosToInstant(timestamp)
                val localDate = timestamp.atZone(ZoneId.systemDefault()).toLocalDate()
                updateStepCount(eventStepCount, timestamp, localDate)
            }
        }

        private fun eventTimestampNanosToInstant(eventTimestampNanos: Long): Instant {
            val nanosecondsSinceEvent = SystemClock.elapsedRealtimeNanos() - eventTimestampNanos
            return Instant.now().minusNanos(nanosecondsSinceEvent)
        }

        private fun updateStepCount(
            newStepCountTotal: Int, timestamp: Instant, localDate: LocalDate
        ) {
            previousStepCount?.let {
                val deltaStepCount = newStepCountTotal - it
                viewModel.incrementStepCount(deltaStepCount, timestamp, localDate)
            }
            previousStepCount = newStepCountTotal
        }

        override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}
    }
}