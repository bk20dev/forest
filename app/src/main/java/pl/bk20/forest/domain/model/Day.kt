package pl.bk20.forest.domain.model

import androidx.room.Entity
import androidx.room.PrimaryKey
import java.time.LocalDate

@Entity(tableName = "day")
data class Day(

    @PrimaryKey val date: LocalDate,

    val steps: Int = 0,

    val goal: Int,

    val height: Int = 188,

    val weight: Int = 70,

    val stepLength: Int = 72,

    val pace: Float = 1f
) {

    companion object

    val carbonDioxideSaved
        get() = run {
            steps / 1000f * 0.1925f
        }

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

fun Day.Companion.of(date: LocalDate, settings: Settings, steps: Int = 0): Day {
    return settings.run {
        Day(
            date = date,
            steps = steps,
            goal = dailyGoal,
            height = height,
            weight = weight,
            stepLength = stepLength,
            pace = pace
        )
    }
}