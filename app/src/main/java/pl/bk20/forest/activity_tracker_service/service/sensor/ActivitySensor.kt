package pl.bk20.forest.activity_tracker_service.service.sensor

import android.hardware.SensorManager

interface ActivitySensor {
    fun registerListener(sensorManager: SensorManager)
    fun unregisterListener(sensorManager: SensorManager)
}