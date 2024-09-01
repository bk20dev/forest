package pl.bk20.forest.activity_tracker_service.data.repository

import android.util.Log
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.consumeAsFlow
import kotlinx.coroutines.flow.filter
import kotlinx.coroutines.flow.filterNotNull
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.flow.runningFold
import kotlinx.coroutines.flow.transformLatest
import pl.bk20.forest.activity_tracker_service.data.entity.StepCountBucket
import pl.bk20.forest.activity_tracker_service.data.entity.TimeBucket
import pl.bk20.forest.activity_tracker_service.data.entity.beginsWith
import pl.bk20.forest.activity_tracker_service.data.entity.extendedWith
import pl.bk20.forest.activity_tracker_service.data.helpers.withPreviousValue
import pl.bk20.forest.activity_tracker_service.data.model.IncrementStepCountEvent
import pl.bk20.forest.activity_tracker_service.domain.repository.StepCountRepository
import java.time.Instant
import java.time.LocalDate
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds
import kotlin.time.toJavaDuration

class StepCountRepositoryImplementation : StepCountRepository {

    companion object {
        val stepCountEventDebounceDuration = 2500.milliseconds
    }

    private val incrementStepCountEventChannel =
        Channel<IncrementStepCountEvent?>(capacity = Channel.UNLIMITED)


    override fun incrementStepCount(deltaStepCount: Int, timestamp: Instant, localDate: LocalDate) {
        val incrementStepCountEvent = IncrementStepCountEvent(
            deltaStepCount = deltaStepCount, timestamp = timestamp, localDate = localDate
        )
        incrementStepCountEventChannel.trySend(incrementStepCountEvent)
    }

    @OptIn(ExperimentalCoroutinesApi::class)
    override suspend fun startStepCountEventAutosave() {
        incrementStepCountEventChannel
            .consumeAsFlow()
            .transformLatest {
                emit(it)
                delay(stepCountEventDebounceDuration)
                emit(null)
            }
            .runningFold<IncrementStepCountEvent?, StepCountBucket?>(null) { stepCountBucket, event ->
                if (event == null) return@runningFold null
                getAccumulatedStepCountBucket(stepCountBucket, event)
            }
            .withPreviousValue()
            .filter { (previous, current) ->
                if (previous == null) false
                else if (current == null) true
                else !current.timeBucket.beginsWith(previous.timeBucket)
            }
            .map {
                it.previous
            }
            .filterNotNull()
            .collect {
                // TODO: Save to the database
                Log.d("StepCountRepositoryImplementation", "Saving bucket $it")
            }
    }

    private fun getAccumulatedStepCountBucket(
        currentStepCountBucket: StepCountBucket?, event: IncrementStepCountEvent
    ): StepCountBucket {
        val stepCountBucketToExtend = currentStepCountBucket?.takeIf {
            it.timeBucket.shouldExtendWith(event, window = stepCountEventDebounceDuration)
        }
        return stepCountBucketToExtend?.updatedWith(event) ?: event.toStepCountBucket()
    }

    private fun TimeBucket.shouldExtendWith(
        event: IncrementStepCountEvent, window: Duration
    ): Boolean = when {
        startLocalDate != event.localDate -> false
        event.timestamp.minus(window.toJavaDuration()).isAfter(endTimestamp) -> false
        event.timestamp.isBefore(startTimestamp) -> false
        else -> true
    }

    private fun StepCountBucket.updatedWith(event: IncrementStepCountEvent): StepCountBucket {
        return copy(
            timeBucket = timeBucket.extendedWith(
                timestamp = event.timestamp,
                localDate = event.localDate,
            ),
            stepCount = stepCount + event.deltaStepCount,
        )
    }

    private fun IncrementStepCountEvent.toStepCountBucket(): StepCountBucket {
        return StepCountBucket(
            timeBucket = TimeBucket(
                startTimestamp = timestamp,
                endTimestamp = timestamp,
                startLocalDate = localDate,
            ), stepCount = deltaStepCount
        )
    }
}