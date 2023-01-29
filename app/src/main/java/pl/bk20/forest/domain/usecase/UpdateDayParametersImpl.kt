package pl.bk20.forest.domain.usecase

import pl.bk20.forest.domain.model.DayParameters
import pl.bk20.forest.domain.repository.DayRepository

class UpdateDayParametersImpl(
    private val repository: DayRepository
) : UpdateDayParameters {

    override suspend fun invoke(dayParameters: DayParameters) {
        repository.updateParameters(dayParameters)
    }
}