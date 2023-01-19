package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Steps
import java.time.LocalDate

interface GetSteps {

    operator fun invoke(date: LocalDate): Flow<Steps?>
}