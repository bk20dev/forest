package pl.bk20.forest.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.*
import pl.bk20.forest.domain.usecase.DayUseCases
import pl.bk20.forest.domain.usecase.SettingsUseCases
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate
import kotlin.math.roundToInt

class StepCounterController(
    private val stepsUseCases: StepsUseCases,
    private val dayUseCases: DayUseCases,
    private val settingsUseCases: SettingsUseCases,
    private val coroutineScope: CoroutineScope,
    initialDate: LocalDate = LocalDate.now()
) {

    private val _stats = MutableStateFlow(StepCounterState(LocalDate.now(), 0, 10000, 0f, 0))
    val stats: StateFlow<StepCounterState> = _stats.asStateFlow()

    private var getStatsJob: Job? = null

    init {
        getStats(initialDate)
    }

    private fun getStats(date: LocalDate) {
        getStatsJob?.cancel()

        val settingsFlow = settingsUseCases.getSettings()
        val dayFlow = dayUseCases.getDay(date)

        getStatsJob = settingsFlow.combine(dayFlow) { settings, day ->
            day?.run {
                stats.value.copy(
                    date = date,
                    steps = steps,
                    goal = goal,
                    distanceTravelled = (settings.stepLength * steps) / 100_000f,
                    calorieBurned = (0.04 * steps * (settings.height / 182.0 + settings.weight / 70.0 - 1) * settings.pace).roundToInt()
                )
            } ?: StepCounterState(
                date = date,
                steps = 0,
                goal = settings.dailyGoal,
                distanceTravelled = 0f,
                calorieBurned = 0
            )
        }.onEach {
            _stats.value = it
        }.launchIn(coroutineScope)
    }

    private val rawStepSensorReadings = MutableStateFlow(StepCounterEvent(0, LocalDate.MIN))
    private var previousStepCount: Int? = null

    init {
        rawStepSensorReadings.drop(1).onEach {
            val stepCountDifference = it.stepCount - (previousStepCount ?: it.stepCount)
            previousStepCount = it.stepCount
            stepsUseCases.incrementStepCount(it.eventDate, stepCountDifference)
        }.launchIn(coroutineScope)
    }

    fun onStepCountChanged(newStepCount: Int, eventDate: LocalDate) {
        rawStepSensorReadings.value = StepCounterEvent(newStepCount, eventDate)
    }
}
