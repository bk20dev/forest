package pl.bk20.forest.trees.domain.usecase

import pl.bk20.forest.core.domain.repository.DayRepository

class ForestUseCases(
    dayRepository: DayRepository
) {

    val getTreeCount: GetTreeCount = GetTreeCountImpl(dayRepository)
}