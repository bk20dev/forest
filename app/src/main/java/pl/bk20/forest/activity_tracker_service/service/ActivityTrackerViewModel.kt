package pl.bk20.forest.activity_tracker_service.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch
import pl.bk20.forest.activity_tracker_service.data.repository.StepCountRepositoryImplementation
import pl.bk20.forest.activity_tracker_service.domain.usecase.ActivityTrackerUseCases
import pl.bk20.forest.activity_tracker_service.domain.usecase.ActivityTrackerUseCasesImplementation
import java.time.Instant
import java.time.LocalDateTime

class ActivityTrackerViewModel(
    private val activityTrackerUseCases: ActivityTrackerUseCases
) : ViewModel() {

    private var activityTrackerAutosaveJob: Job? = null

    fun startActivityAutosave() {
        synchronized(this) {
            if (activityTrackerAutosaveJob != null) {
                return
            }
            activityTrackerAutosaveJob = viewModelScope.launch {
                activityTrackerUseCases.startFitnessMetricsAutosave()
            }
        }
    }

    fun incrementStepCount(deltaStepCount: Int, timestamp: Instant, localDateTime: LocalDateTime) {
        activityTrackerUseCases.updateFitnessMetricsForStepCountDelta(
            deltaStepCount = deltaStepCount,
            timestamp = timestamp,
            localDateTime = localDateTime,
        )
    }

    companion object {
        val Factory: ViewModelProvider.Factory = viewModelFactory {
            initializer {
//                 TODO: Pass all required initializers to the view model
//                 val application = checkNotNull(this[APPLICATION_KEY]) as ForestApplication
                val stepCountRepository = StepCountRepositoryImplementation()
                val activityTrackerUseCases = ActivityTrackerUseCasesImplementation(
                    stepCountRepository = stepCountRepository
                )
                ActivityTrackerViewModel(activityTrackerUseCases)
            }
        }
    }
}