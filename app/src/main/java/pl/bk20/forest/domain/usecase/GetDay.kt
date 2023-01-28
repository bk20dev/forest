package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import java.time.LocalDate

interface GetDay {

    operator fun invoke(date: LocalDate): Flow<Day?>
}