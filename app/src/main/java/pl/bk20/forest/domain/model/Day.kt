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

    val height: Int,

    val weight: Int,

    val stepLength: Int,

    val pace: Float
) {

    val distanceTravelled
        get() = run {
            val distanceCentimeters = steps * stepLength
            distanceCentimeters / 100_000f
        }

    val calorieBurned
        get() = run {
            val modifier = height / 182f + weight / 70f - 1
            0.04f * steps * pace * modifier
        }
}