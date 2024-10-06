package pl.bk20.forest.activity_tracker_service.domain.model

import java.time.Instant
import java.time.LocalDateTime

data class ActivityTimeRange(
    val startInstant: Instant,
    val startLocalDateTime: LocalDateTime,
    val endInstant: Instant,
    val endLocalDateTime: LocalDateTime,
)

fun activityTimeRangeOf(instant: Instant, localDateTime: LocalDateTime): ActivityTimeRange {
    return ActivityTimeRange(
        startInstant = instant,
        startLocalDateTime = localDateTime,
        endInstant = instant,
        endLocalDateTime = localDateTime
    )
}
