package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.StepsRepository

class StepsUseCases(
    repository: StepsRepository
) {

    val incrementStepCount: IncrementStepCount = IncrementStepCountImpl(repository)
}