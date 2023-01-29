package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.model.DayParameters

interface UpdateDayParameters {

    suspend operator fun invoke(dayParameters: DayParameters)
}