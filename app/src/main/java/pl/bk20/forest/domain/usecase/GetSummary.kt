package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.DayRepository
import pl.bk20.forest.presentation.StatsSummary
import pl.bk20.forest.presentation.of

interface GetSummary {
    suspend operator fun invoke(): StatsSummary
}

class GetSummaryImpl(
    private val dayRepository: DayRepository
) : GetSummary {

    override suspend operator fun invoke(): StatsSummary {
        val allDays = dayRepository.getAllDays()
        return StatsSummary.of(allDays)
    }
}