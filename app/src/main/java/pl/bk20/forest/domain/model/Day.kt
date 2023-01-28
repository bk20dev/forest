package pl.bk20.forest.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "day")
data class Day(

    @PrimaryKey
    val date: LocalDate,

    val steps: Int,

    val goal: Int,
)
