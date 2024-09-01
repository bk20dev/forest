package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository

interface StartFitnessMetricsAutosave {
    suspend operator fun invoke()
}

class StartFitnessMetricsAutosaveImplementation(
    private val stepCountRepository: StepCountRepository
) : StartFitnessMetricsAutosave {

    override suspend fun invoke() {
        stepCountRepository.startStepCountEventAutosave()
    }
}