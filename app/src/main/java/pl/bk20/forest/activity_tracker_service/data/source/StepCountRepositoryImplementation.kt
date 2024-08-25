package pl.bk20.forest.activity_tracker_service.data.source

import android.util.Log
import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository
import java.time.Instant

class StepCountRepositoryImplementation : StepCountRepository {
    override fun incrementStepCount(deltaStepCount: Int, timestamp: Instant) {
        Log.d(
            "StepCountRepositoryImplementation",
            "Incrementing step count (by=$deltaStepCount, timestamp=$timestamp)"
        )
    }
}