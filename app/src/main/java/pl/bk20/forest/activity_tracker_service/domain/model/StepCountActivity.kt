package pl.bk20.forest.activity_tracker_service.domain.model

import java.time.Instant
import java.time.LocalDateTime

data class StepCountActivity(
    val id: Long,
    val timeRange: ActivityTimeRange,
    val stepCount: Int,
)

fun stepCountActivityOf(instant: Instant, localDateTime: LocalDateTime): StepCountActivity {
    return StepCountActivity(
        id = 0,
        timeRange = activityTimeRangeOf(instant, localDateTime),
        stepCount = 0,
    )
}