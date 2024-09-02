package pl.bk20.forest.activity_tracker_service.data.model

import java.time.Instant
import java.time.LocalDateTime

data class IncrementStepCountEvent(
    val deltaStepCount: Int,
    val timestamp: Instant,
    val localDateTime: LocalDateTime,
)