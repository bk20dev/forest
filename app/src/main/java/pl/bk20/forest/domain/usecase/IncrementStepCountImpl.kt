package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.firstOrNull
import pl.bk20.forest.domain.model.Steps
import pl.bk20.forest.domain.repository.StepsRepository
import java.time.LocalDate

class IncrementStepCountImpl(
    private val repository: StepsRepository
) : IncrementStepCount {

    override suspend fun invoke(date: LocalDate, by: Int) {
        val steps = repository.getSteps(date).firstOrNull() ?: Steps(date, 0)
        val updatedSteps = steps.copy(count = steps.count + by)
        repository.insertSteps(updatedSteps)
    }
}