package pl.bk20.forest.activity_tracker_service.service

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewModelScope
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.channels.consumeEach
import kotlinx.coroutines.launch
import pl.bk20.forest.activity_tracker_service.data.repository.StepCountRepositoryImplementation
import pl.bk20.forest.activity_tracker_service.domain.model.ActivityTimeRange
import pl.bk20.forest.activity_tracker_service.domain.model.StepCountActivity
import pl.bk20.forest.activity_tracker_service.domain.model.stepCountActivityOf
import pl.bk20.forest.activity_tracker_service.domain.usecase.ActivityTrackerUseCases
import pl.bk20.forest.activity_tracker_service.domain.usecase.ActivityTrackerUseCasesImplementation
import java.time.Instant
import java.time.LocalDateTime
import java.time.temporal.TemporalAmount
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class ActivityTrackerViewModel(
    private val activityTrackerUseCases: ActivityTrackerUseCases
) : ViewModel() {

    companion object {
        private val activityDebounceDuration = 2500.milliseconds

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

    private val incrementStepCountEventQueue =
        Channel<IncrementStepCountEvent>(capacity = Channel.UNLIMITED)

    private var currentStepCountActivity: StepCountActivity? = null

    fun incrementStepCount(deltaStepCount: Int, instant: Instant, localDateTime: LocalDateTime) {
        val incrementStepCountEvent = IncrementStepCountEvent(
            deltaStepCount = deltaStepCount, instant = instant, localDateTime = localDateTime
        )
        incrementStepCountEventQueue.trySend(incrementStepCountEvent)
    }

    init {
        viewModelScope.launch {
            incrementStepCountEventQueue.consumeEach { incrementStepCountEvent ->
                with(incrementStepCountEvent) {
                    updateStepCountActivity(deltaStepCount, instant, localDateTime)
                }
            }
        }
    }

    private suspend fun updateStepCountActivity(
        deltaStepCount: Int, instant: Instant, localDateTime: LocalDateTime
    ) {
        var updatedStepCountActivity: StepCountActivity

        synchronized(this) {
            val stepCountActivity = getStepCountActivity(instant, localDateTime)
            updatedStepCountActivity = stepCountActivity.updatedWith(
                deltaStepCount, instant, localDateTime
            )
            currentStepCountActivity = updatedStepCountActivity
        }

        activityTrackerUseCases.upsertStepCountActivity(updatedStepCountActivity)
    }

    private fun getStepCountActivity(
        instant: Instant, localDateTime: LocalDateTime
    ): StepCountActivity {
        val stepCountActivity = currentStepCountActivity?.takeIf {
            val debouncedTimeRange = it.timeRange.extendedBy(activityDebounceDuration)
            debouncedTimeRange.contains(instant, localDateTime)
        }
        return stepCountActivity ?: stepCountActivityOf(instant, localDateTime)
    }

    private fun ActivityTimeRange.extendedBy(duration: Duration): ActivityTimeRange {
        val temporalAmount: TemporalAmount = duration.toJavaDuration()
        return extendedWith(
            instant = endInstant.plus(temporalAmount),
            localDateTime = endLocalDateTime.plus(temporalAmount)
        )
    }

    private fun ActivityTimeRange.contains(
        instant: Instant, localDateTime: LocalDateTime
    ): Boolean {
        return instant in startInstant..endInstant && localDateTime in startLocalDateTime..endLocalDateTime
    }

    private fun StepCountActivity.updatedWith(
        deltaStepCount: Int, instant: Instant, localDateTime: LocalDateTime
    ): StepCountActivity = copy(
        timeRange = timeRange.extendedWith(instant, localDateTime),
        stepCount = stepCount + deltaStepCount,
    )

    private fun ActivityTimeRange.extendedWith(
        instant: Instant, localDateTime: LocalDateTime
    ): ActivityTimeRange = copy(
        startInstant = minOf(startInstant, instant),
        startLocalDateTime = minOf(startLocalDateTime, localDateTime),
        endInstant = maxOf(endInstant, instant),
        endLocalDateTime = maxOf(endLocalDateTime, localDateTime),
    )

    private data class IncrementStepCountEvent(
        val deltaStepCount: Int,
        val instant: Instant,
        val localDateTime: LocalDateTime,
    )
}