package pl.bk20.forest.domain.repository

import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import pl.bk20.forest.domain.model.DayParameters
import java.time.LocalDate

interface DayRepository {

    fun getDay(date: LocalDate): Flow<Day?>

    suspend fun insertDay(day: Day)

    suspend fun updateParameters(dayParameters: DayParameters)
}