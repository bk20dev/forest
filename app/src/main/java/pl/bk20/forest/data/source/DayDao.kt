package pl.bk20.forest.data.source

import androidx.room.*
import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Day
import pl.bk20.forest.domain.model.DayParameters
import java.time.LocalDate

@Dao
interface DayDao {

    @Query("SELECT * FROM day WHERE date = :date")
    fun getDay(date: LocalDate): Flow<Day?>

    @Upsert
    suspend fun upsertDay(day: Day)

    @Update(entity = Day::class)
    suspend fun updateParameters(day: DayParameters)
}