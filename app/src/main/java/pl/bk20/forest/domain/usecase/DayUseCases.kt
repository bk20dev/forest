package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.repository.DayRepository

class DayUseCases(
    repository: DayRepository
) {

    val getDay: GetDay = GetDayImpl(repository)
}