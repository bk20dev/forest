package pl.bk20.forest.core.domain.usecase

import pl.bk20.forest.core.domain.model.DayParameters
import pl.bk20.forest.core.domain.repository.DayRepository

class UpdateDayParametersImpl(
    private val repository: DayRepository
) : UpdateDayParameters {

    override suspend fun invoke(dayParameters: DayParameters) {
        repository.updateParameters(dayParameters)
    }
}