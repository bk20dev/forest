package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.model.StatsSummary
import pl.bk20.forest.domain.model.of
import pl.bk20.forest.domain.repository.DayRepository

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