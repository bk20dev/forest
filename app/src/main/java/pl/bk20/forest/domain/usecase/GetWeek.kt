package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import java.time.LocalDate

interface GetWeek {

    operator fun invoke(startingAt: LocalDate): Flow<List<Day>>
}
