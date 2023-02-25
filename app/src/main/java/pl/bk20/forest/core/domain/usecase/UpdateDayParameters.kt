package pl.bk20.forest.core.domain.usecase

import pl.bk20.forest.core.domain.model.DayParameters

interface UpdateDayParameters {

    suspend operator fun invoke(dayParameters: DayParameters)
}