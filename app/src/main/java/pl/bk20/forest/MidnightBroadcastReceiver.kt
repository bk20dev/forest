package pl.bk20.forest

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.os.SystemClock
import java.time.LocalDate
import java.time.LocalDateTime
import java.time.temporal.ChronoUnit

class MidnightBroadcastReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context?, intent: Intent?) {
        context?.let {
            updateCurrentDay(it)
            scheduleAlarm(it)
        }
    }

    private fun updateCurrentDay(context: Context) {
        val application = context.applicationContext as ForestApplication
        application.currentDate.value = LocalDate.now()
    }

    private val millisToMidnight
        get(): Long {
            val currentDateTime = LocalDateTime.now()
            val midnightDateTime = currentDateTime.plusDays(1).truncatedTo(ChronoUnit.DAYS)
            return currentDateTime.until(midnightDateTime, ChronoUnit.MILLIS)
        }

    private fun getAlarmIntent(context: Context): PendingIntent = Intent(context, javaClass).let {
        val flags = PendingIntent.FLAG_IMMUTABLE
        PendingIntent.getBroadcast(context, 0, it, flags)
    }

    private fun scheduleAlarm(context: Context) {
        val triggerAtMillis = SystemClock.elapsedRealtime() + millisToMidnight
        val alarmIntent = getAlarmIntent(context)

        val alarmManager = context.getSystemService(Context.ALARM_SERVICE) as AlarmManager
        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.ELAPSED_REALTIME, triggerAtMillis, alarmIntent
        )
    }
}