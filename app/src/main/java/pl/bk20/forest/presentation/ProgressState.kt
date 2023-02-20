package pl.bk20.forest.presentation

import java.time.LocalDate

data class ProgressState(
    val date: LocalDate,
    val stepsTaken: Int,
    val dailyGoal: Int,
    val carbonDioxideSaved: Float,
    val calorieBurned: Int,
    val distanceTravelled: Float
)