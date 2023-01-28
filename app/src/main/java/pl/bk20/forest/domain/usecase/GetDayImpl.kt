package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import pl.bk20.forest.domain.repository.DayRepository
import java.time.LocalDate

class GetDayImpl(
    private val repository: DayRepository
) : GetDay {

    override fun invoke(date: LocalDate): Flow<Day?> {
        return repository.getDay(date)
    }
}