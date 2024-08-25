package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository
import java.time.Instant

interface UpdateFitnessMetricsForStepCountDelta {
    operator fun invoke(deltaStepCount: Int, timestamp: Instant)
}

class UpdateFitnessMetricsForStepCountDeltaImplementation(
    private val stepCountRepository: StepCountRepository
) : UpdateFitnessMetricsForStepCountDelta {

    override fun invoke(deltaStepCount: Int, timestamp: Instant) {
        stepCountRepository.incrementStepCount(deltaStepCount, timestamp)
    }
}