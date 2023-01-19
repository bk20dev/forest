package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Steps
import pl.bk20.forest.domain.repository.StepsRepository
import java.time.LocalDate

class GetStepsImpl(
    private val repository: StepsRepository
) : GetSteps {

    override fun invoke(date: LocalDate): Flow<Steps?> {
        return repository.getSteps(date)
    }
}