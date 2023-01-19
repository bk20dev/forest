package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.StepsRepository

class StepsUseCases(
    repository: StepsRepository
) {

    val getSteps: GetSteps = GetStepsImpl(repository)
    val incrementStepCount: IncrementStepCount = IncrementStepCountImpl(repository)
}