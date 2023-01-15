package pl.bk20.forest.data.source

import androidx.room.Dao
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow
import pl.bk20.forest.domain.model.Steps
import java.time.LocalDate

@Dao
interface StepsDao {

    @Query("SELECT * FROM step WHERE date = :date")
    fun getSteps(date: LocalDate): Flow<Steps?>

    @Update
    suspend fun updateSteps(steps: Steps)
}