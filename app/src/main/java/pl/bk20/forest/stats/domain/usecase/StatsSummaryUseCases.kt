package pl.bk20.forest.stats.domain.usecase

import pl.bk20.forest.core.domain.repository.DayRepository

class StatsSummaryUseCases(
    dayRepository: DayRepository
) {

    val getSummary: GetSummary = GetSummaryImpl(dayRepository)
}