package pl.bk20.forest.activity_tracker_service.domain.repository

import java.time.Instant

interface StepCountRepository {
    fun incrementStepCount(deltaStepCount: Int, timestamp: Instant)
}