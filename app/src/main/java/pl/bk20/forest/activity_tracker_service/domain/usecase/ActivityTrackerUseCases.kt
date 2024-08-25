package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository

interface ActivityTrackerUseCases {
    val updateFitnessMetricsForStepCountDelta: UpdateFitnessMetricsForStepCountDelta
}

class ActivityTrackerUseCasesImplementation(
    stepCountRepository: StepCountRepository,
) : ActivityTrackerUseCases {

    override val updateFitnessMetricsForStepCountDelta =
        UpdateFitnessMetricsForStepCountDeltaImplementation(
            stepCountRepository = stepCountRepository
        )
}