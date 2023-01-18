package pl.bk20.forest.data.source

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import pl.bk20.forest.domain.model.Steps
import java.time.LocalDate

@Dao
interface StepsDao {

    @Query("SELECT * FROM step WHERE date = :date")
    suspend fun getSteps(date: LocalDate): Steps?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertSteps(steps: Steps)
}