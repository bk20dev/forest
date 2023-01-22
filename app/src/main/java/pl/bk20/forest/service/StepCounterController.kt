package pl.bk20.forest.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Job
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate

class StepCounterController(
    private val stepsUseCases: StepsUseCases,
    private val coroutineScope: CoroutineScope
) {

    private val _steps = MutableStateFlow(StepCounterState(LocalDate.now(), 0, 10000, 0f, 0))
    val steps: StateFlow<StepCounterState> = _steps.asStateFlow()

    private var getStepsJob: Job? = null

    init {
        updateActiveDate(steps.value.date)
    }

    fun updateActiveDate(date: LocalDate) {
        getSteps(date)
    }

    private fun getSteps(date: LocalDate) {
        getStepsJob?.cancel()
        getStepsJob = stepsUseCases.getSteps(date).onEach {
            val newStepCount = it?.count ?: 0
            _steps.value = steps.value.copy(
                date = date,
                takenSteps = newStepCount,
                distanceTravelledInKm = newStepCount * 7f / 10000, // 7 dm per step
                calorieBurned = newStepCount / 25 // 0.04 (1/25) kcal per step
            )
        }.launchIn(coroutineScope)
    }

    private var previousStepCount: Int? = null

    fun onStepCountChanged(newStepCount: Int, eventDate: LocalDate) {
        val stepCountDifference = newStepCount - (previousStepCount ?: newStepCount)
        previousStepCount = newStepCount

        coroutineScope.launch {
            stepsUseCases.incrementStepCount(eventDate, stepCountDifference)
        }
    }
}
