package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository
import java.time.Instant
import java.time.LocalDate

interface UpdateFitnessMetricsForStepCountDelta {
    operator fun invoke(deltaStepCount: Int, timestamp: Instant, localDate: LocalDate)
}

class UpdateFitnessMetricsForStepCountDeltaImplementation(
    private val stepCountRepository: StepCountRepository
) : UpdateFitnessMetricsForStepCountDelta {

    override fun invoke(deltaStepCount: Int, timestamp: Instant, localDate: LocalDate) {
        stepCountRepository.incrementStepCount(deltaStepCount, timestamp, localDate)
    }
}