package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository
import java.time.Instant
import java.time.LocalDateTime

interface UpdateFitnessMetricsForStepCountDelta {
    operator fun invoke(deltaStepCount: Int, timestamp: Instant, localDateTime: LocalDateTime)
}

class UpdateFitnessMetricsForStepCountDeltaImplementation(
    private val stepCountRepository: StepCountRepository
) : UpdateFitnessMetricsForStepCountDelta {

    override fun invoke(deltaStepCount: Int, timestamp: Instant, localDateTime: LocalDateTime) {
        stepCountRepository.incrementStepCount(deltaStepCount, timestamp, localDateTime)
    }
}