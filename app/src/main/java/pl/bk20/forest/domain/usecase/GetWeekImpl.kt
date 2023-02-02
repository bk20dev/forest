package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import pl.bk20.forest.domain.repository.DayRepository
import java.time.LocalDate

class GetWeekImpl(
    private val dayRepository: DayRepository
) : GetWeek {

    override fun invoke(startingAt: LocalDate): Flow<List<Day>> {
        val endingAt = startingAt.plusDays(6)
        return dayRepository.getDays(startingAt..endingAt)
    }
}