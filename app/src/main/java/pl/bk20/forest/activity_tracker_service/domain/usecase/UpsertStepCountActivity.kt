package pl.bk20.forest.activity_tracker_service.domain.usecase

import pl.bk20.forest.activity_tracker_service.domain.model.StepCountActivity
import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository

interface UpsertStepCountActivity {
    suspend operator fun invoke(activity: StepCountActivity)
}

class UpsertStepCountActivityImplementation(
    private val stepCountRepository: StepCountRepository
) : UpsertStepCountActivity {

    override suspend operator fun invoke(activity: StepCountActivity) {
        stepCountRepository.upsertStepCountActivity(activity)
    }
}