package pl.bk20.forest.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import pl.bk20.forest.domain.model.DayParameters
import java.time.LocalDate

interface DayRepository {

    fun getFirstDay(): Flow<Day?>

    fun getDay(date: LocalDate): Flow<Day?>

    fun getDays(range: ClosedRange<LocalDate>): Flow<List<Day>>

    suspend fun upsertDay(day: Day)

    suspend fun updateParameters(dayParameters: DayParameters)
}