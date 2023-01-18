package pl.bk20.forest.service

import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import pl.bk20.forest.domain.usecase.StepsUseCases
import java.time.LocalDate

class StepCounterController(
    private val stepsUseCases: StepsUseCases,
    private val coroutineScope: CoroutineScope
) {

    private var previousStepCount: Int? = null

    fun onStepCountChanged(newStepCount: Int, eventDate: LocalDate) {
        val stepCountDifference = newStepCount - (previousStepCount ?: newStepCount)
        previousStepCount = newStepCount

        coroutineScope.launch {
            stepsUseCases.incrementStepCount(eventDate, stepCountDifference)
        }
    }
}
