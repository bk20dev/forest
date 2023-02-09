package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.DayRepository

class StatsUseCases(
    dayRepository: DayRepository,
) {

    val getWeek: GetWeek = GetWeekImpl(dayRepository)
    val getFirstDayDate: GetFirstDate = GetFirstDateImpl(dayRepository)
}