package pl.bk20.forest.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import kotlinx.coroutines.launch
import pl.bk20.forest.core.domain.usecase.DayUseCases
import java.time.LocalDate
import kotlin.math.roundToInt

class StepCounterController(
    private val dayUseCases: DayUseCases,
    private val coroutineScope: CoroutineScope,
    currentDateFlow: StateFlow<LocalDate>,
) {

    private val _stats = MutableStateFlow(StepCounterState(LocalDate.now(), 0, 0, 0.0, 0))
    val stats: StateFlow<StepCounterState> = _stats.asStateFlow()

    private var getStatsJob: Job? = null

    init {
        coroutineScope.launch {
            currentDateFlow.collect { getStats(it) }
        }
    }

    private fun getStats(date: LocalDate) {
        getStatsJob?.cancel()

        getStatsJob = dayUseCases.getDay(date).onEach { day ->
            _stats.value = day.run {
                StepCounterState(
                    date = date,
                    steps = steps,
                    goal = goal,
                    distanceTravelled = distanceTravelled,
                    calorieBurned = calorieBurned.roundToInt()
                )
            }
        }.launchIn(coroutineScope)
    }

    private val rawStepSensorReadings = MutableStateFlow(StepCounterEvent(0, LocalDate.MIN))
    private var previousStepCount: Int? = null

    init {
        rawStepSensorReadings.drop(1).onEach { event ->
            val stepCountDifference = event.stepCount - (previousStepCount ?: event.stepCount)
            previousStepCount = event.stepCount
            dayUseCases.incrementStepCount(event.eventDate, stepCountDifference)
        }.launchIn(coroutineScope)
    }

    fun onStepCountChanged(newStepCount: Int, eventDate: LocalDate) {
        rawStepSensorReadings.value = StepCounterEvent(newStepCount, eventDate)
    }
}
