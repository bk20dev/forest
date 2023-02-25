package pl.bk20.forest.core.domain.util

import java.time.Duration
import java.time.Instant
import java.time.temporal.ChronoUnit

class MidnightTimer(
    private val timer: Timer,
    private val task: TimerTask
) {
    fun start() {
        val delay = millisToMidnight(Instant.now())
        timer.schedule(task, delay, Duration.ofDays(1))
    }

    fun stop() {
        timer.cancel()
    }

    private fun millisToMidnight(at: Instant): Long {
        val midnight = at.truncatedTo(ChronoUnit.DAYS) + Duration.ofDays(1)
        return midnight.toEpochMilli() - at.toEpochMilli()
    }
}