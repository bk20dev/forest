package pl.bk20.forest.core.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.core.domain.model.Day
import java.time.LocalDate

interface GetDay {

    operator fun invoke(date: LocalDate): Flow<Day>
}