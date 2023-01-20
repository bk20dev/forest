package pl.bk20.forest.domain.util

import java.time.Duration

class TimerImpl : Timer {

    private var instance: java.util.Timer? = null

    override fun schedule(task: TimerTask, delay: Long, period: Duration) {
        instance?.cancel()
        val timerTask = object : java.util.TimerTask() {
            override fun run() {
                task.run()
            }
        }
        instance = java.util.Timer().also {
            it.schedule(timerTask, delay, period.toMillis())
        }
    }

    override fun cancel() {
        instance?.cancel()
    }
}