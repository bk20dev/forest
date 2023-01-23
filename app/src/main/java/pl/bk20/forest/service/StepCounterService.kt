package pl.bk20.forest.service

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.Build
import android.os.Build.VERSION_CODES
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import kotlinx.coroutines.launch
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.R
import pl.bk20.forest.data.repository.StepsRepositoryImpl
import pl.bk20.forest.domain.usecase.StepsUseCases
import pl.bk20.forest.domain.util.MidnightTimer
import pl.bk20.forest.domain.util.TimerImpl
import pl.bk20.forest.presentation.MainActivity
import java.time.LocalDate

class StepCounterService : LifecycleService(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var controller: StepCounterController

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 0x1
        private const val PENDING_INTENT_ID = 0x1
    }

    private val midnightTimer = MidnightTimer(TimerImpl()) {
        val today = LocalDate.now()
        controller.updateActiveDate(today)
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationChannel = createNotificationChannel()
            registerNotificationChannel(notificationChannel)
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        registerStepCounter(sensorManager)

        // Initialise controller
        val stepsDatabase = (application as ForestApplication).stepsDatabase
        val stepsRepository = StepsRepositoryImpl(stepsDatabase.stepsDao)
        val useCases = StepsUseCases(stepsRepository)
        controller = StepCounterController(useCases, lifecycleScope)

        // Create notification
        val notification = createNotification(controller.steps.value)
        startForeground(NOTIFICATION_ID, notification)

        midnightTimer.start()

        val notificationManager = getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        lifecycleScope.launch {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                controller.steps.collect {
                    val updatedNotification = createNotification(it)
                    notificationManager.notify(NOTIFICATION_ID, updatedNotification)
                }
            }
        }
    }

    private fun createNotification(state: StepCounterState): Notification = state.run {
        val title = resources.getQuantityString(R.plurals.step_count, takenSteps, takenSteps)
        val progress = takenSteps * 100 / dailyGoal
        val content = getString(
            R.string.step_counter_stats, calorieBurned, distanceTravelledInKm, progress
        )

        NotificationCompat.Builder(this@StepCounterService, NOTIFICATION_CHANNEL_ID)
            .setContentIntent(launchApplicationPendingIntent)
            .setSmallIcon(R.drawable.baseline_directions_walk_24)
            .setContentTitle(title)
            .setContentText(content)
            .setOnlyAlertOnce(true)
            .setOngoing(true)
            .build()
    }

    private val launchApplicationPendingIntent
        get(): PendingIntent {
            val intent = Intent(applicationContext, MainActivity::class.java)
            val flags = PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
            return PendingIntent.getActivity(this, PENDING_INTENT_ID, intent, flags)
        }

    private fun registerStepCounter(sensorManager: SensorManager) {
        val stepCounterSensor: Sensor? = sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)
        stepCounterSensor?.let {
            sensorManager.registerListener(this, it, SensorManager.SENSOR_DELAY_NORMAL)
        }
    }

    override fun onSensorChanged(event: SensorEvent?) {
        event?.let {
            val eventStepCount = it.values[0].toInt()
            controller.onStepCountChanged(eventStepCount, LocalDate.now())
        }
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {}

    override fun onDestroy() {
        super.onDestroy()
        midnightTimer.stop()
        sensorManager.unregisterListener(this)
    }

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