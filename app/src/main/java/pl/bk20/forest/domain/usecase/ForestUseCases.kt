package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.DayRepository

class ForestUseCases(
    dayRepository: DayRepository
) {

    val getTreeCount: GetTreeCount = GetTreeCountImpl(dayRepository)
}