package pl.bk20.forest.activity_tracker_service.data.repository

import android.util.Log
import pl.bk20.forest.activity_tracker_service.domain.model.StepCountActivity
import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository

class StepCountRepositoryImplementation : StepCountRepository {

    override suspend fun upsertStepCountActivity(stepCountActivity: StepCountActivity) {
        Log.d("StepCountRepositoryImplementation", "Saving step count activity $stepCountActivity")
    }
}