package pl.bk20.forest.core.domain.usecase

import pl.bk20.forest.core.domain.repository.DayRepository

class StatsUseCases(
    dayRepository: DayRepository,
) {

    val getWeek: GetWeek = GetWeekImpl(dayRepository)
    val getFirstDayDate: GetFirstDate = GetFirstDateImpl(dayRepository)
}