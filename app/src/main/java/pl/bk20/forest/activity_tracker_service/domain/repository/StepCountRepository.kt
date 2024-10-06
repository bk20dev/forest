package pl.bk20.forest.activity_tracker_service.domain.repository

import pl.bk20.forest.activity_tracker_service.domain.model.StepCountActivity

interface StepCountRepository {
    suspend fun upsertStepCountActivity(stepCountActivity: StepCountActivity)
}