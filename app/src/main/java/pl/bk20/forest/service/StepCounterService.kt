package pl.bk20.forest.service

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
import androidx.annotation.RequiresApi
import androidx.core.app.NotificationCompat
import androidx.lifecycle.LifecycleService
import androidx.lifecycle.lifecycleScope
import pl.bk20.forest.ForestApplication
import pl.bk20.forest.R
import pl.bk20.forest.data.repository.StepsRepositoryImpl
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate

class StepCounterService : LifecycleService(), SensorEventListener {

    private lateinit var sensorManager: SensorManager
    private lateinit var controller: StepCounterController

    companion object {
        private const val NOTIFICATION_CHANNEL_ID = "step_counter_channel"
        private const val NOTIFICATION_ID = 0x1
    }

    override fun onCreate() {
        super.onCreate()
        if (Build.VERSION.SDK_INT >= VERSION_CODES.O) {
            val notificationChannel = createNotificationChannel()
            registerNotificationChannel(notificationChannel)
        }
        sensorManager = getSystemService(Context.SENSOR_SERVICE) as SensorManager
        registerStepCounter(sensorManager)
        val notification = createNotification()
        startForeground(NOTIFICATION_ID, notification)
        // Initialise controller
        val stepsDatabase = (application as ForestApplication).stepsDatabase
        val stepsRepository = StepsRepositoryImpl(stepsDatabase.stepsDao)
        val useCases = StepsUseCases(stepsRepository)
        controller = StepCounterController(useCases, lifecycleScope)
    }

    private fun createNotification(): Notification =
        NotificationCompat.Builder(this, NOTIFICATION_CHANNEL_ID)
            .setOngoing(true)
            .build()

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