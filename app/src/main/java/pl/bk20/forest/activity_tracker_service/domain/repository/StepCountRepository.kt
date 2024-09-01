package pl.bk20.forest.activity_tracker_service.domain.repository

import java.time.Instant
import java.time.LocalDate

interface StepCountRepository {
    suspend fun startStepCountEventAutosave()
    fun incrementStepCount(deltaStepCount: Int, timestamp: Instant, localDate: LocalDate)
}