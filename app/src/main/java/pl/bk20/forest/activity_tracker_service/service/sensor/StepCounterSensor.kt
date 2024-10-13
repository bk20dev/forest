package pl.bk20.forest.activity_tracker_service.service.sensor

import android.hardware.Sensor
import android.hardware.SensorEvent
import android.hardware.SensorEventListener
import android.hardware.SensorManager
import android.os.SystemClock
import java.time.Instant
import java.time.LocalDateTime
import java.time.ZoneId

fun interface OnStepCountChangeListener {
    fun onStepCountChange(deltaStepCount: Int, instant: Instant, localDateTime: LocalDateTime)
}

class StepCounterSensor(
    private val stepCountChangeListener: OnStepCountChangeListener
) : SensorEventListener {

    private var previousStepCountTotal: Int? = null

    override fun onSensorChanged(event: SensorEvent?) {
        event?.run {
            val newStepCountTotal = values[0].toInt()
            val instant = eventTimestampNanosToInstant(timestamp)
            val localDateTime = instantToSystemDefaultLocalDateTime(instant)

            previousStepCountTotal?.let {
                val deltaStepCount = newStepCountTotal - it
                stepCountChangeListener.onStepCountChange(deltaStepCount, instant, localDateTime)
            }
            previousStepCountTotal = newStepCountTotal
        }
    }

    fun registerListener(sensorManager: SensorManager) {
        sensorManager.getDefaultSensor(Sensor.TYPE_STEP_COUNTER)?.let { sensor ->
            sensorManager.registerListener(
                this, sensor, SensorManager.SENSOR_DELAY_NORMAL
            )
        }
    }

    fun unregisterListener(sensorManager: SensorManager) {
        sensorManager.unregisterListener(this)
    }

    override fun onAccuracyChanged(sensor: Sensor?, accuracy: Int) {
    }

    private fun eventTimestampNanosToInstant(eventTimestampNanos: Long): Instant {
        val nanosecondsSinceEvent = SystemClock.elapsedRealtimeNanos() - eventTimestampNanos
        return Instant.now().minusNanos(nanosecondsSinceEvent)
    }

    private fun instantToSystemDefaultLocalDateTime(instant: Instant): LocalDateTime {
        val userTimezone = ZoneId.systemDefault()
        return instant.atZone(userTimezone).toLocalDateTime()
    }
}