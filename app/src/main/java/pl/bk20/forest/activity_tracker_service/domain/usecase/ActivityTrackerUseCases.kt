package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository

interface ActivityTrackerUseCases {
    val startFitnessMetricsAutosave: StartFitnessMetricsAutosave
    val updateFitnessMetricsForStepCountDelta: UpdateFitnessMetricsForStepCountDelta
}

class ActivityTrackerUseCasesImplementation(
    stepCountRepository: StepCountRepository,
) : ActivityTrackerUseCases {

    override val startFitnessMetricsAutosave =
        StartFitnessMetricsAutosaveImplementation(
            stepCountRepository = stepCountRepository,
        )

    override val updateFitnessMetricsForStepCountDelta =
        UpdateFitnessMetricsForStepCountDeltaImplementation(
            stepCountRepository = stepCountRepository,
        )
}