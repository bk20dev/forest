package pl.bk20.forest.domain.usecase

import kotlinx.coroutines.flow.Flow
import java.time.LocalDate

interface GetFirstDate {

    operator fun invoke(): Flow<LocalDate?>
}